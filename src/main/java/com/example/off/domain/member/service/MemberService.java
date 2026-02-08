package com.example.off.domain.member.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.dto.*;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.off.domain.member.Member.NICKNAME_MAX_LENGTH;
import static com.example.off.domain.member.Member.SELF_INTRO_MAX_LENGTH;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final Clock clock; //현재 시점
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    LocalDateTime now = LocalDateTime.now(clock);

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(Long memberId){
        Member member = findMember(memberId); //회원 찾기
        //현재 시점을 기준으로 진행중인 project 찾기
//        boolean isWorking = projectMemberRepository.existsWorkingProject(memberId, now);
        Optional<ProjectMember> workingProjectList = projectMemberRepository.findWorkingProject(memberId, now);
        if (workingProjectList.isEmpty()) //진행 중인 프로젝트 없음.
            return ProfileResponse.of(member, null);

        if (!Boolean.TRUE.equals(member.getIsWorking())) { //실제 프로젝트 진행중이지만 member.isWorking == false 인 경우
            log.warn("회원 {}의 isWorking 값이 실제와 달라 자동 보정합니다.", memberId);
            member.startWorking();
        }

        //프로젝트를 진행 중인 경우
        String workingProjectName = workingProjectList.get().getProject().getName();
        return ProfileResponse.of(member, workingProjectName);
    }

    @Transactional(readOnly = true)
    public MyProjectsResponse getMyProjects(Long memberId){
        //참여했던 플젝 list
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByMember_Id(memberId);
        return MyProjectsResponse.from(projectMembers);
    }

    @Transactional
    public UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest updateReq){
        Member member = findMember(memberId);
        String nickname = updateReq.nickname();

        //nickname 수정
        //빈 문자열 금지, 중복 허용하지 않음
        if (nickname!=null && !nickname.isBlank()){
            validateNickname(nickname);
            member.updateNickname(nickname);
        }

        //프로젝트 경험 횟수 수정
        if (updateReq.projectCount()!=null)
            member.updateProjectCount(updateReq.projectCount());

        //포트폴리오 수정
        if (updateReq.portfolioList()!=null){
            //기존 포폴 모두 삭제 후 새로 생성, 저장
            member.getPortfolios().clear();

            for (PortfolioRequest pr : updateReq.portfolioList()) {
                //des, link 모두 빈 문자열일 경우 저장하지 않음
                String description = pr.description() == null ? "" : pr.description();
                String link = pr.link() == null ? "" : pr.link();
                if (description.isBlank() && link.isBlank()) {
                    continue;
                }

                Portfolio portfolio = Portfolio.of(
                        pr.description(),
                        pr.link()
                );
                member.getPortfolios().add(portfolio);
            }
        }

        //자기소개 수정 (빈 문자열 허용)
        String selfIntroduction = updateReq.selfIntroduction();
        if (selfIntroduction!=null){
            validateIntroduction(selfIntroduction);
            member.updateIntroduction(selfIntroduction);
        }
        return UpdateProfileResponse.from(member);
    }

    //memberId로 회원찾기
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new OffException(ResponseCode.MEMBER_NOT_FOUND));
    }

    private void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname))
            throw new OffException(ResponseCode.DUPLICATE_NICKNAME);

        if (nickname.length() > NICKNAME_MAX_LENGTH)
            throw new OffException(ResponseCode.INVALID_INPUT_VALUE);
    }

    private void validateIntroduction(String selfIntroduction) {
        if (selfIntroduction.length() > SELF_INTRO_MAX_LENGTH)
            throw new OffException(ResponseCode.INVALID_INPUT_VALUE);
    }
}
