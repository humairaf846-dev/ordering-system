package com.example.orderingsystem.boundary;

import java.math.BigDecimal;

public interface PaymentGatewayAPI {
    boolean prosesPembayaran(BigDecimal jumlah, String metodePembayaran);
}