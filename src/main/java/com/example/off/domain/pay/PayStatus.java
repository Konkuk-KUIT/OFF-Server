package com.example.off.domain.pay;

public enum PayStatus {
    READY,
    PAID,        // 결제 완료
    FAILED,      // 결제 실패
    CANCELLED    // 요청 취소 또는 결제 취소
}
