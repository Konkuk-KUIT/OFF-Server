package com.example.off.domain.pay.dto;

public record ConfirmPayRequest(
        String orderId,
        String paymentKey,
        Long amount
) {
}
