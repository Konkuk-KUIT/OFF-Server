package com.example.off.common.auth;

import java.security.Principal;

// Spring Security 없이 사용하는 아주 단순한 신분증
public class StompPrincipal implements Principal {
    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name; // 여기 저장된 값이 곧 memberId가 됩니다.
    }
}
