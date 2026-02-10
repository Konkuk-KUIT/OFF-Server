package com.example.off.domain.pay.controller;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.pay.dto.*;
import com.example.off.domain.pay.service.PayLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class PayController {
    private final PayLogService payLogService;

    @PostMapping("/payments/prepare")
    public BaseResponse<PreparePayResponse> prepare(
            @Valid @RequestBody PreparePayRequest req,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(payLogService.prepare(memberId, req));
    }

    @PostMapping("/payments/confirm")
    public BaseResponse<ConfirmPayResponse> confirm(
            @Valid @RequestBody ConfirmPayRequest req,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(payLogService.confirm(memberId, req));
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = (Long) req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
