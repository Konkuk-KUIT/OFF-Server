package com.example.off.domain.member.dto;

import com.example.off.domain.pay.PayLog;
import com.example.off.domain.pay.PayStatus;
import com.example.off.domain.projectMember.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
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
        private Long amount;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;

        public static ProjectItem from(ProjectMember pm){
            //가장 마지막 결제만을 가져옴 (FAILED 등 제외)
            PayLog latestPay = pm.getPayLogs().stream()
                    .filter(pay -> pay.getStatus() == PayStatus.PAID)
                    .max(Comparator.comparing(PayLog::getPaidAt))
                    .orElse(null);

            return new ProjectItem(
                    pm.getProject().getId(),
                    pm.getProject().getName(),
                    latestPay != null ? latestPay.getAmount() : null,
                    latestPay != null ? latestPay.getPaidAt() : null,
                    latestPay != null ? latestPay.getCreatedAt() : null
            );
        }
    }
}
