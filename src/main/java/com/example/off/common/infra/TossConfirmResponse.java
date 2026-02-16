package com.example.off.common.infra;

public record TossConfirmResponse(
        String paymentKey,
        String orderId,
        String status,
        Long totalAmount
) {}
