package com.example.off.domain.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateProjectRequest {
    private String name;
    private String description;
    private Long projectTypeId;
    private String requirement;
    private List<RecruitmentRequest> recruitmentList;

    @Getter
    @NoArgsConstructor
    public static class RecruitmentRequest {
        private String roleId;
        private Integer count;
    }
}