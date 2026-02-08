package com.example.off.domain.project;

import lombok.Getter;

@Getter
public enum ProjectType {
    APP(1L, "앱"),
    SERVICE(2L, "서비스"),
    CONTENTS(3L, "콘텐츠"),
    PRODUCT(4L, "제품");

    private final Long id;
    private final String displayName;

    ProjectType(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static ProjectType fromId(Long id) {
        for (ProjectType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 프로젝트 유형 ID: " + id);
    }
}
