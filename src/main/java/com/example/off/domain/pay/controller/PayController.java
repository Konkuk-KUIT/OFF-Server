package com.example.off.domain.pay.controller;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.pay.dto.CreatePayLogRequest;
import com.example.off.domain.pay.dto.CreatePayLogResponse;
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
    @PostMapping("/payments")
    public BaseResponse<CreatePayLogResponse> createPayLog(@Valid @RequestBody CreatePayLogRequest req,
                                                           HttpServletRequest httpServletRequest){
            Long memberId = getMemberId(httpServletRequest);
            CreatePayLogResponse response = payLogService.createPayLog(memberId, req);
            return BaseResponse.ok(response);
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
