package com.example.off.domain.pay.dto;

import com.example.off.domain.pay.PayStatus;

public record ConfirmPayResponse(
    long payLogId,
    PayStatus status
) {
}
