package com.example.off.domain.project.dto;

import com.example.off.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateProjectResponse {
    private String projectType;
    private List<String> recruitmentRoles;
    private String endDate;
    private String serviceSummary;
    private Long totalEstimate;  // 원 단위
    private List<EstimateResponse> estimateList;

    @Getter
    @AllArgsConstructor
    public static class EstimateResponse {
        private String role;
        private Long cost;  // 원 단위
        private Integer count;
        private List<PartnerResponse> candidatePartners;

        public static EstimateResponse of(String role, Long cost, Integer count, List<PartnerResponse> partners) {
            return new EstimateResponse(role, cost, count, partners);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PartnerResponse {
        private Long memberId;
        private String nickname;
        private String introduction;
        private Integer projectCount;
        private Long suggestedCost;  // AI가 추천한 적정 가격 (원 단위)

        public static PartnerResponse of(Member member, Long suggestedCost) {
            return new PartnerResponse(
                    member.getId(),
                    member.getNickname(),
                    member.getSelfIntroduction(),
                    member.getProjectCount().getCount(),
                    suggestedCost
            );
        }
    }
}