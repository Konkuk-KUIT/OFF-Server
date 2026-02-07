package com.example.off.domain.member.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest (
    @NotNull
    String email,
    @NotNull
    String password
){}
