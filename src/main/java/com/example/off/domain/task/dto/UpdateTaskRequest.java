package com.example.off.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
