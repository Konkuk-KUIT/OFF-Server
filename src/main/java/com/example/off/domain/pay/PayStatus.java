package com.example.off.domain.pay;

public enum PayStatus {
    REQUESTED,   // 결제 요청됨 (상대 수락 대기)
    APPROVED,    // 수락 완료 (결제 진행 준비)
    PAID,        // 결제 완료
    FAILED,      // 결제 실패
    CANCELLED    // 요청 취소 또는 결제 취소
}
