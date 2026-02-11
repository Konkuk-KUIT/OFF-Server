package com.example.off.domain.pay.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.notification.NotificationType;
import com.example.off.domain.notification.service.NotificationService;
import com.example.off.domain.partnerRecruit.ApplicationStatus;
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

    @Transactional
    public PreparePayResponse prepare(Long memberId, PreparePayRequest req) {
        Member payer = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        PartnerApplication application = partnerApplicationRepository.findById(req.applicationId())
                .orElseThrow(() -> new OffException(ResponseCode.APPLICATION_NOT_FOUND));

        // 초대(isFromProject=true)인 경우: 파트너가 ACCEPT해야 결제 가능
        // 지원(isFromProject=false)인 경우: 기획자가 바로 결제 가능 (WAITING 상태)
        if (application.getIsFromProject() && application.getApplicationStatus() != ApplicationStatus.ACCEPT) {
            throw new OffException(ResponseCode.INVALID_APPLICATION_STATUS);
        }
        if (!application.getIsFromProject() && application.getApplicationStatus() != ApplicationStatus.WAITING) {
            throw new OffException(ResponseCode.INVALID_APPLICATION_STATUS);
        }

        PartnerRecruit recruit = application.getPartnerRecruit();

        // recruit.cost는 해당 프로젝트에서 직군별로 고정된 가격
        long amount = (long) recruit.getCost();

        String orderId = "order_" + UUID.randomUUID().toString().replace("-", "");

        PayLog payLog = PayLog.ready(orderId, amount, payer, application);
        payLogRepository.save(payLog);

        String orderName = "파트너 매칭 결제";
        return new PreparePayResponse(orderId, amount, orderName);
    }

    @Transactional(readOnly = true)
    public PayLog validateForConfirm(String orderId) {
        PayLog payLog = payLogRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OffException(ResponseCode.PAYLOG_NOT_FOUND));

        if (payLog.getStatus() == PayStatus.PAID) {
            return payLog;
        }

        if (payLog.getStatus() != PayStatus.READY) {
            throw new OffException(ResponseCode.INVALID_PAY_STATUS);
        }

        return payLog;
    }

    @Transactional
    public void markFailed(Long payLogId) {
        PayLog payLog = payLogRepository.findById(payLogId)
                .orElseThrow(() -> new OffException(ResponseCode.PAYLOG_NOT_FOUND));
        payLog.markFailed();
    }

    @Transactional
    public ConfirmPayResponse completePayment(Long payLogId, String paymentKey) {
        PayLog payLog = payLogRepository.findById(payLogId)
                .orElseThrow(() -> new OffException(ResponseCode.PAYLOG_NOT_FOUND));

        if (payLog.getStatus() == PayStatus.PAID) {
            return new ConfirmPayResponse(payLog.getId(), PayStatus.PAID);
        }

        PartnerApplication application = payLog.getApplication();

        // 지원(isFromProject=false)인 경우 결제 시점에 자동 승인
        if (!application.getIsFromProject() && application.getApplicationStatus() == ApplicationStatus.WAITING) {
            application.accept();
        }

        PartnerRecruit recruit = partnerRecruitRepository.findByIdForUpdate(application.getPartnerRecruit().getId())
                .orElseThrow(() -> new OffException(ResponseCode.RECRUIT_NOT_FOUND));
        Project project = recruit.getProject();
        Member payee = application.getMember();

        ProjectMember projectMember = ProjectMember.of(project, payee);
        projectMemberRepository.save(projectMember);

        recruit.downNumberOfPerson();
        recruit.closeIfFull();

        payLog.markPaid(paymentKey, projectMember);

        notificationService.sendNotification(
                payee.getId(),
                "결제 성공! 파트너 매칭이 되었어요.",
                "/projects/" + project.getId(),
                NotificationType.PAY
        );

        return new ConfirmPayResponse(payLog.getId(), PayStatus.PAID);
    }
}
