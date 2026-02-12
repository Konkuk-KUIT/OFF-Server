package com.example.off.domain.partnerRecruit.dto;

import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.ProjectCountType;
import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PartnerProfileResponse {
    private Long memberId;
    private String name;
    private String nickname;
    private String profileImage;
    private Role role;
    private String selfIntroduction;
    private Boolean isWorking;
    private ProjectCountType projectCount;
    private List<PortfolioItem> portfolios;

    public static PartnerProfileResponse of(Member member) {
        List<PortfolioItem> portfolios = member.getPortfolios() == null
                ? List.of()
                : member.getPortfolios().stream()
                .map(PortfolioItem::from)
                .toList();

        return new PartnerProfileResponse(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getProfileImage(),
                member.getRole(),
                member.getSelfIntroduction(),
                member.getIsWorking(),
                member.getProjectCount(),
                portfolios
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PortfolioItem {
        private String description;
        private String link;

        public static PortfolioItem from(Portfolio portfolio) {
            return new PortfolioItem(portfolio.getDescription(), portfolio.getLink());
        }
    }
}
