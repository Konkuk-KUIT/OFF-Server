package com.example.off.domain.pay.service;

import com.example.off.common.exception.OffException;
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
import com.example.off.domain.pay.dto.CreatePayLogRequest;
import com.example.off.domain.pay.dto.CreatePayLogResponse;
import com.example.off.domain.pay.repository.PayLogRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PayLogService {
    private final PayLogRepository payLogRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final NotificationService notificationService;

    public CreatePayLogResponse createPayLog(Long memberId, @Valid CreatePayLogRequest req) {
        // 결제자
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        // 파트너 지원/요청
        PartnerApplication application = partnerApplicationRepository.findById(req.applicationId())
                .orElseThrow(() -> new OffException(ResponseCode.APPLICATION_NOT_FOUND));
        // 공고
        PartnerRecruit recruit = application.getPartnerRecruit();
        // 파트너
        Member payee = application.getMember();
        // 프로젝트
        Project project = application.getPartnerRecruit().getProject();
        // 프로젝트 멤버 생성, 저장
        ProjectMember projectMember = ProjectMember.of(project, payee);
        projectMemberRepository.save(projectMember);
        // 공고에서 모집 인원 감소 -> estimation 값을 따로 프로젝트에서 저장하므로 변경해도 괜찮을 거예여 아마
        recruit.downNumberOfPerson();
        // 결제 금액
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = project.getEnd();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        double months = days / 30.0;
        long totalCost = (long) (recruit.getCost() * months);
        // 결제 내역 저장
        PayLog payLog = PayLog.of(totalCost, PayStatus.PAID, member, projectMember);
        payLogRepository.save(payLog);
        // 알림 발송
        notificationService.sendNotification(payee.getId(), "결제 성공! 파트너 매칭이 되었어요.",
                "/projects/" + project.getId().toString(), NotificationType.PAY);
        return new CreatePayLogResponse(LocalDateTime.now(), payLog.getId());
    }
}
