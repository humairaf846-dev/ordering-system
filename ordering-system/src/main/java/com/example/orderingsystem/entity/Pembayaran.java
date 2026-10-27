package com.example.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pembayaran")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pembayaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pesanan_id")
    private Pesanan pesanan;

    private String metodePembayaran;
    private BigDecimal jumlahBayar;
    private String statusPembayaran;
    private LocalDateTime tanggalPembayaran;
}