package com.example.off.domain.pay.dto;

import java.time.LocalDateTime;

public record CreatePayLogResponse(
        LocalDateTime createAt,
        Long payLogId
) {

}
