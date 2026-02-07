package com.example.off.domain.member.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.dto.*;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(Long memberId){
        Member member = findMember(memberId); //회원 찾기

        //진행중인 project 찾기
        if (!Boolean.TRUE.equals(member.getIsWorking()))
            return ProfileResponse.of(member, null);

        //진행 중인 경우 분기 처리
        List<ProjectMember> projectMembers = findMyProjects(memberId);
        if (projectMembers.isEmpty()) { //isWorking==true 인데 projectMember 가 없는 경우.
            log.error("회원 {}의 프로젝트 진행 여부와 프로젝트 정보가 일치하지 않습니다.", memberId);
            throw new OffException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        String projectName = projectMembers.getFirst().getProject().getName();
        return ProfileResponse.of(member, projectName);
    }

    @Transactional(readOnly = true)
    public MyProjectsResponse getMyProjects(Long memberId){
        Member member = findMember(memberId);
        //참여했던 플젝 list
        List<ProjectMember> projectMembers = findMyProjects(memberId);
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
                        pr.link(),
                        member
                );
                member.getPortfolios().add(portfolio);
            }
        }

        //자기소개 수정 (빈 문자열 허용)
        if (updateReq.selfIntroduction()!=null){
            member.updateIntroduction(updateReq.selfIntroduction());
        }
        return UpdateProfileResponse.from(member);
    }

    //memberId로 회원찾기
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new OffException(ResponseCode.MEMBER_NOT_FOUND));
    }

    //memberId로 진행중인 project 리스트 찾아오기
    public List<ProjectMember> findMyProjects(Long memberId) {
        return projectMemberRepository.findAllByMember_Id(memberId);
    }

    private void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname))
            throw new OffException(ResponseCode.DUPLICATE_NICKNAME);
    }
}
