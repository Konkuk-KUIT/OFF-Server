package com.example.off.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ConfirmProjectRequest {
    @NotBlank
    private String name;
    private String description;
    private Long projectTypeId = 1L;  // 앱개발로 고정 (1L = APP)
    private String requirement;
    private String serviceSummary;
    @NotBlank
    private String endDate;
    @NotNull
    @Positive
    private Long totalEstimate;  // 원 단위
    @NotNull
    private List<RecruitmentRequest> recruitmentList;

    @Getter
    @NoArgsConstructor
    public static class RecruitmentRequest {
        @NotBlank
        private String roleId;
        @NotNull
        @Positive
        private Integer count;
        @NotNull
        @Positive
        private Long cost;  // 원 단위 (필수)
        private List<Long> selectedPartnerIds;  // 선택한 파트너 ID 목록 (선택 사항)
    }
}
