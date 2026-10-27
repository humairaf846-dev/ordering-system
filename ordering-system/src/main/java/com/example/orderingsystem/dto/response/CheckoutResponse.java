package com.example.orderingsystem.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutResponse {
    private Long pesananId;
    private String statusPesanan;
    private BigDecimal totalHarga;
    private String metodePembayaran;
    private String statusPembayaran;
}