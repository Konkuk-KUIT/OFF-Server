package com.example.off.domain.partnerRecruit.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.notification.NotificationType;
import com.example.off.domain.notification.service.NotificationService;
import com.example.off.domain.partnerRecruit.*;
import com.example.off.domain.partnerRecruit.dto.*;
import com.example.off.domain.partnerRecruit.repository.PartnerApplicationRepository;
import com.example.off.domain.partnerRecruit.repository.PartnerRecruitRepository;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.pay.repository.PayLogRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.project.repository.ProjectRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import com.example.off.domain.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerMatchingService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final PayLogRepository payLogRepository;
    private final NotificationService notificationService;

    @Transactional
    public InvitePartnerResponse invite(Long memberId, Long projectId, InvitePartnerRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        if (!project.getCreator().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        Member partner = memberRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        Role role = parseRole(request.getRole());

        PartnerRecruit recruit = partnerRecruitRepository.findByProjectAndRole(project, role)
                .orElseThrow(() -> new OffException(ResponseCode.RECRUIT_NOT_FOUND));

        if (recruit.getRecruitStatus() != RecruitStatus.OPEN) {
            throw new OffException(ResponseCode.RECRUIT_CLOSED);
        }

        if (partnerApplicationRepository.existsByMemberAndPartnerRecruit(partner, recruit)) {
            throw new OffException(ResponseCode.ALREADY_APPLIED);
        }

        PartnerApplication application = PartnerApplication.of(partner, recruit, true);
        partnerApplicationRepository.save(application);

        notificationService.sendNotification(
                partner.getId(),
                project.getName() + " 프로젝트에서 파트너 제안이 도착했어요!",
                "/invitations/" + application.getId(),
                NotificationType.INVITE
        );

        return InvitePartnerResponse.of(application.getId());
    }

    @Transactional
    public AcceptInvitationResponse acceptInvitation(Long memberId, Long invitationId) {
        PartnerApplication application = partnerApplicationRepository.findById(invitationId)
                .orElseThrow(() -> new OffException(ResponseCode.APPLICATION_NOT_FOUND));

        if (!application.getMember().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        if (application.getApplicationStatus() != ApplicationStatus.WAITING) {
            throw new OffException(ResponseCode.INVALID_APPLICATION_STATUS);
        }

        application.accept();

        PartnerRecruit recruit = partnerRecruitRepository.findByIdForUpdate(application.getPartnerRecruit().getId())
                .orElseThrow(() -> new OffException(ResponseCode.RECRUIT_NOT_FOUND));

        if (recruit.getRecruitStatus() != RecruitStatus.OPEN) {
            throw new OffException(ResponseCode.RECRUIT_CLOSED);
        }

        Project project = recruit.getProject();
        Member partner = application.getMember();

        if (projectMemberRepository.existsByProjectAndMember(project, partner)) {
            throw new OffException(ResponseCode.ALREADY_PROJECT_MEMBER);
        }

        ProjectMember projectMember = ProjectMember.of(project, partner);
        projectMemberRepository.save(projectMember);

        recruit.downNumberOfPerson();
        recruit.closeIfFull();

        notificationService.sendNotification(
                project.getCreator().getId(),
                partner.getNickname() + "님이 파트너 제안을 수락했어요!",
                "/projects/" + project.getId(),
                NotificationType.INVITE
        );

        return AcceptInvitationResponse.of(projectMember.getId());
    }

    @Transactional
    public ApplyProjectResponse apply(Long memberId, Long projectId, ApplyProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        if (project.getCreator().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        Member applicant = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        Role role = parseRole(request.getRole());

        PartnerRecruit recruit = partnerRecruitRepository.findByProjectAndRole(project, role)
                .orElseThrow(() -> new OffException(ResponseCode.RECRUIT_NOT_FOUND));

        if (recruit.getRecruitStatus() != RecruitStatus.OPEN) {
            throw new OffException(ResponseCode.RECRUIT_CLOSED);
        }

        if (partnerApplicationRepository.existsByMemberAndPartnerRecruit(applicant, recruit)) {
            throw new OffException(ResponseCode.ALREADY_APPLIED);
        }

        PartnerApplication application = PartnerApplication.of(applicant, recruit, false);
        partnerApplicationRepository.save(application);

        notificationService.sendNotification(
                project.getCreator().getId(),
                applicant.getNickname() + "님이 " + role.getValue() + " 역할로 지원했어요!",
                "/projects/" + project.getId() + "/applications/" + application.getId(),
                NotificationType.APPLICATION
        );

        return ApplyProjectResponse.of(application.getId());
    }

    @Transactional
    public AcceptApplicationResponse acceptApplication(Long memberId, Long projectId, Long applicationId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        if (!project.getCreator().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        PartnerApplication application = partnerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new OffException(ResponseCode.APPLICATION_NOT_FOUND));

        if (application.getApplicationStatus() != ApplicationStatus.WAITING) {
            throw new OffException(ResponseCode.INVALID_APPLICATION_STATUS);
        }

        application.accept();

        PartnerRecruit recruit = application.getPartnerRecruit();
        long amount = (long) recruit.getCost();

        String orderId = "order_" + UUID.randomUUID().toString().replace("-", "");
        Member payer = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        PayLog payLog = PayLog.ready(orderId, amount, payer, application);
        payLogRepository.save(payLog);

        String orderName = "파트너 매칭 결제";
        return AcceptApplicationResponse.of(orderId, amount, orderName);
    }

    @Transactional(readOnly = true)
    public PartnerProfileResponse getPartnerProfile(Long partnerId) {
        Member partner = memberRepository.findById(partnerId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        return PartnerProfileResponse.of(partner);
    }

    private Role parseRole(String roleStr) {
        return switch (roleStr.toLowerCase()) {
            case "planner" -> Role.PM;
            case "developer" -> Role.DEV;
            case "designer" -> Role.DES;
            case "marketer" -> Role.MAR;
            default -> {
                try {
                    yield Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new OffException(ResponseCode.INVALID_ROLE);
                }
            }
        };
    }
}
