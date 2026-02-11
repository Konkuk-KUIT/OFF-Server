package com.example.off.domain.pay.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.infra.TossConfirmResponse;
import com.example.off.common.infra.TossPaymentsClient;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.pay.PayStatus;
import com.example.off.domain.pay.dto.ConfirmPayRequest;
import com.example.off.domain.pay.dto.ConfirmPayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayFacade {
    private final PayLogService payLogService;
    private final TossPaymentsClient tossPaymentsClient;

    public ConfirmPayResponse confirm(Long memberId, ConfirmPayRequest req) {
        // 1. 검증 (읽기 트랜잭션)
        PayLog payLog = payLogService.validateForConfirm(req.orderId());

        // 이미 결제 완료 → 멱등 처리
        if (payLog.getStatus() == PayStatus.PAID) {
            return new ConfirmPayResponse(payLog.getId(), PayStatus.PAID);
        }

        // 2. Toss API 호출 (트랜잭션 밖)
        TossConfirmResponse tossResponse;
        try {
            tossResponse = tossPaymentsClient.confirm(
                    req.paymentKey(),
                    req.orderId(),
                    req.amount()
            );
        } catch (Exception e) {
            payLogService.markFailed(payLog.getId());
            throw new OffException(ResponseCode.TOSS_CONFIRM_FAILED);
        }

        if (tossResponse == null || !"DONE".equalsIgnoreCase(tossResponse.status())) {
            payLogService.markFailed(payLog.getId());
            throw new OffException(ResponseCode.TOSS_CONFIRM_FAILED);
        }

        // 3. DB 확정 (쓰기 트랜잭션)
        return payLogService.completePayment(payLog.getId(), req.paymentKey());
    }
}
