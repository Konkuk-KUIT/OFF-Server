package com.example.off.domain.project.dto;

import com.example.off.domain.project.ProjectStatus;
import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {
    private Long projectId;
    private String name;
    private String description;
    private String introduction;
    private String startDate;
    private String endDate;
    private long dDay;
    private ProjectStatus status;
    private int progressPercent;
    private List<RecruitSummary> recruits;
    private List<TaskSummary> tasks;
    private List<MemberSummary> members;

    @Getter
    @AllArgsConstructor
    public static class RecruitSummary {
        private Long recruitId;
        private Role role;
        private int numberOfPerson;
        private String recruitStatus;
    }

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {
        private Long taskId;
        private String name;
        private String description;
        private String assigneeName;
        private int progressPercent;
        private List<ToDoSummary> toDoList;
    }

    @Getter
    @AllArgsConstructor
    public static class ToDoSummary {
        private Long toDoId;
        private String content;
        private boolean isDone;
    }

    @Getter
    @AllArgsConstructor
    public static class MemberSummary {
        private Long memberId;
        private String nickname;
        private String profileImage;
        private Role role;
    }
}
