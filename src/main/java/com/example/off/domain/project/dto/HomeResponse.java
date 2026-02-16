package com.example.off.domain.project.dto;

import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private boolean showCreateButton;  // PM이고 진행 중인 프로젝트가 없을 때 true
    private List<MyProjectSummary> projects;
    private List<PartnerRecommendation> partners;

    @Getter
    @AllArgsConstructor
    public static class MyProjectSummary {
        private Long projectId;
        private String name;
        private String creatorNickname;  // 기획자 닉네임
        private String endDate;
        private long dDay;
        private int progressPercent;
        private boolean isRecruiting;  // 모집 중인지 여부
        private List<RecruitInfo> recruitList;  // 모집 중인 역할 목록
    }

    @Getter
    @AllArgsConstructor
    public static class RecruitInfo {
        private Role role;  // 모집 역할
        private int count;  // 모집 인원
    }

    @Getter
    @AllArgsConstructor
    public static class PartnerRecommendation {
        private Long memberId;
        private String nickname;
        private String profileImage;
        private Role role;
        private String selfIntroduction;
        private String projectCount;  // 프로젝트 경험 횟수 (예: "3회", "5회 이상")
        private int portfolioCount;    // 포트폴리오 개수
    }
}
