package com.example.orderingsystem.boundary.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.orderingsystem.boundary.PaymentGatewayAPI;

@Component
public class PaymentGatewayAPIImpl implements PaymentGatewayAPI {

    @Override
    public boolean prosesPembayaran(BigDecimal jumlah, String metodePembayaran) {
        // Simulasi dummy payment gateway selalu berhasil
        return true;
    }
}