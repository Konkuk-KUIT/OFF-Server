package com.example.off.domain.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToggleToDoResponse {
    private Long toDoId;
    @JsonProperty("isDone")
    private boolean isDone;
    private int taskProgressPercent;

    public static ToggleToDoResponse of(Long toDoId, boolean isDone, int taskProgressPercent) {
        return new ToggleToDoResponse(toDoId, isDone, taskProgressPercent);
    }
}
