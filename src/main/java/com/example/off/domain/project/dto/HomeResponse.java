package com.example.off.domain.project.dto;

import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private List<MyProjectSummary> projects;
    private List<PartnerRecommendation> partners;
    private boolean hasMorePartners;

    @Getter
    @AllArgsConstructor
    public static class MyProjectSummary {
        private Long projectId;
        private String name;
        private String endDate;
        private long dDay;
        private int progressPercent;
    }

    @Getter
    @AllArgsConstructor
    public static class PartnerRecommendation {
        private Long memberId;
        private String nickname;
        private String profileImage;
        private Role role;
        private String selfIntroduction;
    }
}
