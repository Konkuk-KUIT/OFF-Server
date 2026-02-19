package com.example.off.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToggleToDoResponse {
    private Long toDoId;
    private boolean isDone;
    private int taskProgressPercent;

    public static ToggleToDoResponse of(Long toDoId, boolean isDone, int taskProgressPercent) {
        return new ToggleToDoResponse(toDoId, isDone, taskProgressPercent);
    }
}
