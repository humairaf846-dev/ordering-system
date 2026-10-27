package com.example.orderingsystem.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CheckoutRequest {
    private Long pelangganId;
    private List<ItemRequest> items;
    private String metodePembayaran;

    @Data
    public static class ItemRequest {
        private Long menuId;
        private Integer jumlah;
    }
}