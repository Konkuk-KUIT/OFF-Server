package com.example.off.domain.member.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.dto.MyProjectsResponse;
import com.example.off.domain.member.dto.PortfolioRequest;
import com.example.off.domain.member.dto.ProfileResponse;
import com.example.off.domain.member.dto.UpdateProfileRequest;
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
        //회원 찾기
        Member member = findMember(memberId);

        //진행중인 project 찾기
        if (!Boolean.TRUE.equals(member.getIsWorking()))
            return ProfileResponse.of(member, null);

        //진행 중인 경우 분기 처리
        List<ProjectMember> projectMembers = findMyProjects(memberId);

        if (projectMembers.isEmpty()) { //isWorking==true 인데 projectMember 가 없는 경우.
            log.error("회원 {}의 프로젝트 진행 여부와 프로젝트 정보가 일치하지 않습니다.", memberId    );
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

    public void updateProfile(Long memberId, UpdateProfileRequest updateReq){
        Member member = findMember(memberId);

        //Todo: 리팩토링 필요
        //nickname 수정
        //빈 문자열 금지, 중복 허용하지 않음
        if (updateReq.getNickname()!=null && !updateReq.getNickname().isBlank()){
            if (memberRepository.existsByNickname(updateReq.getNickname()))
                throw new OffException(ResponseCode.DUPLICATE_NICKNAME);
            member.updateNickname(updateReq.getNickname());
        }

        //프로젝트 경험 횟수 수정
        if (updateReq.getProjectCountType()!=null)
            member.updateProjectCount(updateReq.getProjectCountType());

        //포트폴리오 수정
        if (updateReq.getPortfolioRequests()!=null){
            //기존 포폴 모두 삭제 후 새로 생성, 저장
            member.getPortfolios().clear();

            for (PortfolioRequest pr : updateReq.getPortfolioRequests()) {
                Portfolio portfolio = Portfolio.of(
                        pr.getDescription(),
                        pr.getLink(),
                        member
                );
                member.getPortfolios().add(portfolio);
            }
        }

        //자기소개 수정 (빈 문자열 허용)
        if (updateReq.getNickname()!=null){
            member.updateIntroduction(updateReq.getSelfIntroduction());
        }
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
}
