package com.example.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pesanan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelanggan_id", nullable = false)
    private Pelanggan pelanggan;

    @Column(nullable = false)
    private LocalDateTime tanggalPesanan;

    @Column(nullable = false, length = 20)
    private String statusPesanan;

    @Column(nullable = false)
    private BigDecimal totalHarga;

    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL)
    private List<ItemPesanan> listItemPesanan;

    @OneToOne(mappedBy = "pesanan", cascade = CascadeType.ALL)
    private Pembayaran pembayaran;
}