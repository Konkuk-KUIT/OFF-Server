package com.example.off.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Todo: PortfolioReqeust/Resposne 통합 (ProfileResponse inner class)
public record PortfolioRequest(
        String description,
        String link
) {}