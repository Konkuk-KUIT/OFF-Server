package com.example.off.domain.pay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PreparePayRequest(
        @NotNull @Positive Long applicationId
) {
}
