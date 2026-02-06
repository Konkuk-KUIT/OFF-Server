package com.example.off.domain.member.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.dto.ProfileResponse;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new OffException(ResponseCode.MEMBER_NOT_FOUND));

        //진행중인 project 찾기
        if (!Boolean.TRUE.equals(member.getIsWorking()))
                return ProfileResponse.of(member, null);

        //진행 중인 경우 분기 처리
        List<ProjectMember> projectMembers =
                projectMemberRepository.findAllByMember_Id(memberId); //memberId 가 참가하는 모든 플젝명

        if (projectMembers.isEmpty()) { //isWorking==true 인데 projectMember 가 없는 경우.
            log.error("회원 {}의 프로젝트 진행 여부와 프로젝트 정보가 일치하지 않습니다.", memberId    );
            throw new OffException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        String projectName = projectMembers.getFirst().getProject().getName();
        return ProfileResponse.of(member, projectName);
    }
}
