package com.example.off.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record PortfolioRequest(
        String description,
        String link
) {}