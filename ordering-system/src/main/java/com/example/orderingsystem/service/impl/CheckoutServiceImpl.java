package com.example.orderingsystem.service.impl;

import com.example.orderingsystem.boundary.PaymentGatewayAPI;
import com.example.orderingsystem.dto.request.CheckoutRequest;
import com.example.orderingsystem.dto.response.CheckoutResponse;
import com.example.orderingsystem.entity.*;
import com.example.orderingsystem.exception.BusinessValidationException;
import com.example.orderingsystem.repository.*;
import com.example.orderingsystem.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final PelangganRepository pelangganRepository;
    private final MenuRepository menuRepository;
    private final PesananRepository pesananRepository;
    private final PembayaranRepository pembayaranRepository;
    private final PaymentGatewayAPI paymentGatewayAPI;

    @Override
    @Transactional
    public CheckoutResponse processCheckout(CheckoutRequest request) {
        Pelanggan pelanggan = pelangganRepository.findById(request.getPelangganId())
                .orElseThrow(() -> new BusinessValidationException("Pelanggan tidak ditemukan"));

        Pesanan pesanan = Pesanan.builder()
                .pelanggan(pelanggan)
                .tanggalPesanan(LocalDateTime.now())
                .statusPesanan("PENDING")
                .totalHarga(BigDecimal.ZERO)
                .listItemPesanan(new ArrayList<>())
                .build();

        BigDecimal totalHarga = BigDecimal.ZERO;

        for (CheckoutRequest.ItemRequest itemReq : request.getItems()) {
            Menu menu = menuRepository.findById(itemReq.getMenuId())
                    .orElseThrow(() -> new BusinessValidationException("Menu tidak ditemukan"));

            // Validasi stok menggunakan BusinessValidationException
            if (menu.getStok() < itemReq.getJumlah()) {
                throw new BusinessValidationException("Stok tidak mencukupi untuk menu: " + menu.getNamaMenu() + " (Sisa: " + menu.getStok() + ")");
            }

            BigDecimal subtotal = menu.getHarga().multiply(BigDecimal.valueOf(itemReq.getJumlah()));
            totalHarga = totalHarga.add(subtotal);

            ItemPesanan itemPesanan = ItemPesanan.builder()
                    .pesanan(pesanan)
                    .menu(menu)
                    .jumlah(itemReq.getJumlah())
                    .subtotal(subtotal)
                    .build();

            pesanan.getListItemPesanan().add(itemPesanan);
        }

        pesanan.setTotalHarga(totalHarga);

        // Cek pembayaran sebelum mengurangi stok secara permanen
        boolean isPaid;
        try {
            isPaid = paymentGatewayAPI.prosesPembayaran(totalHarga, request.getMetodePembayaran());
        } catch (Exception ex) {
            throw new BusinessValidationException("Pembayaran gagal diproses: " + ex.getMessage());
        }

        if (!isPaid) {
            throw new BusinessValidationException("Pembayaran ditolak oleh Gateway Pembayaran");
        }

        // Jika sukses, kurangi stok menu dan simpan data
        for (ItemPesanan item : pesanan.getListItemPesanan()) {
            Menu menu = item.getMenu();
            menu.setStok(menu.getStok() - item.getJumlah());
            menuRepository.save(menu);
        }

        pesanan.setStatusPesanan("COMPLETED");
        Pesanan savedPesanan = pesananRepository.save(pesanan);

        Pembayaran pembayaran = Pembayaran.builder()
                .pesanan(savedPesanan)
                .metodePembayaran(request.getMetodePembayaran())
                .jumlahBayar(totalHarga)
                .statusPembayaran("SUCCESS")
                .tanggalPembayaran(LocalDateTime.now())
                .build();

        pembayaranRepository.save(pembayaran);

        return CheckoutResponse.builder()
                .pesananId(savedPesanan.getId())
                .statusPesanan(savedPesanan.getStatusPesanan())
                .totalHarga(totalHarga)
                .metodePembayaran(request.getMetodePembayaran())
                .statusPembayaran(pembayaran.getStatusPembayaran())
                .build();
    }
}