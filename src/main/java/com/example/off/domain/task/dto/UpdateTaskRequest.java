package com.example.off.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Long projectMemberId;

    private List<ToDoItem> toDoList;  // 세부태스크 목록 (null이면 수정 안 함)

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ToDoItem {
        private Long id;  // null이면 신규 생성, 있으면 기존 업데이트
        @NotBlank
        private String content;
    }
}
