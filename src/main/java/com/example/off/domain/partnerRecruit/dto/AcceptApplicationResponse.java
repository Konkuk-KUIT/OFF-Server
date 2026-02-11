package com.example.off.domain.partnerRecruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptApplicationResponse {
    private String orderId;
    private long amount;
    private String orderName;

    public static AcceptApplicationResponse of(String orderId, long amount, String orderName) {
        return new AcceptApplicationResponse(orderId, amount, orderName);
    }
}
