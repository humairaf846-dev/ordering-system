package com.example.orderingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pesanan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pesanan_id")
    private Pesanan pesanan;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer jumlah;
    private BigDecimal hargaSatuan;
    private BigDecimal subtotal;
}