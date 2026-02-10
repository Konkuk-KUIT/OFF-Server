package com.example.off.domain.pay.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.infra.TossConfirmResponse;
import com.example.off.common.infra.TossPaymentsClient;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.notification.NotificationType;
import com.example.off.domain.notification.service.NotificationService;
import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.partnerRecruit.PartnerRecruit;
import com.example.off.domain.partnerRecruit.repository.PartnerApplicationRepository;
import com.example.off.domain.partnerRecruit.repository.PartnerRecruitRepository;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.pay.PayStatus;
import com.example.off.domain.pay.dto.*;
import com.example.off.domain.pay.repository.PayLogRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayLogService {
    private final PayLogRepository payLogRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final NotificationService notificationService;
    private final TossPaymentsClient tossPaymentsClient; // ← 이거

    @Transactional
    public PreparePayResponse prepare(Long memberId, PreparePayRequest req) {
        // 결제자
        Member payer = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // 지원서
        PartnerApplication application = partnerApplicationRepository.findById(req.applicationId())
                .orElseThrow(() -> new OffException(ResponseCode.APPLICATION_NOT_FOUND));

        // 공고/프로젝트
        PartnerRecruit recruit = application.getPartnerRecruit();
        Project project = recruit.getProject();

        // 금액 계산 (30일 = 1개월)
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = project.getEnd();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        double months = days / 30.0;

        long amount = (long) (recruit.getCost() * months);

        // orderId 생성
        String orderId = "order_" + UUID.randomUUID().toString().replace("-", "");

        // PayLog(READY) 저장
        // 중요: READY 단계에서는 projectMember가 아직 없으므로 null 허용이어야 함
        PayLog payLog = PayLog.ready(orderId, amount, payer, application);
        payLogRepository.save(payLog);

        // 프론트가 결제창 띄울 때 필요한 값 반환
        String orderName = "파트너 매칭 결제";
        return new PreparePayResponse(orderId, amount, orderName);
    }

    @Transactional
    public ConfirmPayResponse confirm(Long memberId, ConfirmPayRequest req) {
        // 결제자
        Member payer = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        PayLog payLog = payLogRepository.findByOrderId(req.orderId())
                .orElseThrow(() -> new OffException(ResponseCode.PAYLOG_NOT_FOUND));

        // 이미 결제 완료 → 멱등 처리
        if (payLog.getStatus() == PayStatus.PAID) {
            return new ConfirmPayResponse(payLog.getId(), PayStatus.PAID);
        }

        // READY 상태 아니면 오류
        if (payLog.getStatus() != PayStatus.READY) {
            throw new OffException(ResponseCode.INVALID_PAY_STATUS);
        }

        TossConfirmResponse tossResponse;
        try {
            tossResponse = tossPaymentsClient.confirm(
                    req.paymentKey(),
                    req.orderId(),
                    req.amount()
            );
        } catch (Exception e) {
            payLog.markFailed();
            throw new OffException(ResponseCode.TOSS_CONFIRM_FAILED);
        }

        if (tossResponse == null || !"DONE".equalsIgnoreCase(tossResponse.status())) {
            payLog.markFailed();
            throw new OffException(ResponseCode.TOSS_CONFIRM_FAILED);
        }

        // === 결제 성공 후 도메인 확정 ===
        PartnerApplication application = payLog.getApplication();
        PartnerRecruit recruit = application.getPartnerRecruit();
        Project project = recruit.getProject();
        Member payee = application.getMember();

        // ProjectMember 생성
        ProjectMember projectMember = ProjectMember.of(project, payee);
        projectMemberRepository.save(projectMember);

        // 모집 인원 감소
        recruit.downNumberOfPerson();

        // PayLog 확정
        payLog.markPaid(req.paymentKey(), projectMember);

        // 알림
        notificationService.sendNotification(
                payee.getId(),
                "결제 성공! 파트너 매칭이 되었어요.",
                "/projects/" + project.getId(),
                NotificationType.PAY
        );

        return new ConfirmPayResponse(payLog.getId(), PayStatus.PAID);
    }
}
