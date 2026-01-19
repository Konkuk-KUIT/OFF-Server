package com.example.off.domain.member;

import lombok.Getter;

@Getter
public enum ProjectCountType {
    ZERO("0회", 0),
    ONCE("1회", 1),
    TWICE("2회", 2),
    THREE_TIMES("3회", 3),
    FOUR_TIMES("4회", 4),
    PLUS_FIVE("5회 이상", 5);

    private final String value;
    private final int count;

    ProjectCountType(String value, int count) {
        this.value = value;
        this.count = count;
    }
}
