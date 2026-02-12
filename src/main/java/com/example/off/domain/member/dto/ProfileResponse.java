package com.example.off.domain.member.dto;

import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.ProjectCountType;
import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    //프로필 조회용
    private String name;
    private String nickname; //공통 필드
    private String profileImage;  // 프로필 이미지
    private Boolean isWorking;
    private String projectName;  //내가 속한 플젝명
    private Role role;

    //수정하기 메타 데이터
    private ProjectCountType projectCount;
    private List<PortfolioResponse> portfolioList;
    private String selfIntroduction;


    public static ProfileResponse of(Member member, String projectName){
        //List<Portfolio> -> List<PortfolioResponse> 로 변환
        List<PortfolioResponse> portfolioResponses =
                member.getPortfolios() == null
                        ? List.of()
                        : member.getPortfolios().stream()
                        .map(PortfolioResponse::from)
                        .toList();

        return new ProfileResponse(
                member.getName(),
                member.getNickname(),
                member.getProfileImage(),
                member.getIsWorking(),
                projectName,
                member.getRole(),
                member.getProjectCount(),
                portfolioResponses,
                member.getSelfIntroduction()
        );
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PortfolioResponse {
        private String description;
        private String link;

        public static PortfolioResponse from(Portfolio portfolio){
            return new PortfolioResponse(
                    portfolio.getDescription(),
                    portfolio.getLink()
            );
        }
    }
}
