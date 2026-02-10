package com.example.off.domain.pay.dto;

public record PreparePayResponse(
        String orderId,
        Long amount,
        String orderName
) {
}
