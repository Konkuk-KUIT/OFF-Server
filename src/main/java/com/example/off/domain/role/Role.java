package com.example.off.domain.role;

import lombok.Getter;

@Getter
public enum Role {
    PM("기획자"),
    DEV("개발자"),
    DES("디자이너"),
    MAR("마케터");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
