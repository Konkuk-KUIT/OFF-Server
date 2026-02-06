package com.example.off.domain.member.dto;

import com.example.off.domain.projectMember.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MyProjectsResponse {
    private List<ProjectItem> projectList;

    public static MyProjectsResponse from(List<ProjectMember> projectMembers){
        //ProjectMember -> ProjectItem 으로 list type 변환
        List<ProjectItem> projectItemList =
                projectMembers.stream()
                        .map(ProjectItem::from)
                        .toList();

        return new MyProjectsResponse(
                projectItemList
        );
    }


    @Getter
    @AllArgsConstructor
    private static class ProjectItem {
        private Long id;
        private String name;
//        private Long amount;
//        private LocalDateTime paidAt;
        private LocalDateTime createdAt;
        //Todo: 도메인에 paidAt 컬럼, amount 추가 필요

        //TODO: amount, paidAt 추가
        public static ProjectItem from(ProjectMember pm){
            return new ProjectItem(
                    pm.getProject().getId(),
                    pm.getProject().getName(),
                    pm.getCreatedAt()
            );
        }
    }
}
