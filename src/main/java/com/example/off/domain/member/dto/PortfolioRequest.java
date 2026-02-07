package com.example.off.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest {
    //Todo: null 허용여부
    private String description;
    private String link;
}