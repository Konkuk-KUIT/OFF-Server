package com.example.off.domain.member.dto;
import com.example.off.domain.member.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDto {
    private String description;
    private String link;

    public static PortfolioDto from(Portfolio portfolio){
        return new PortfolioDto(
                portfolio.getDescription(),
                portfolio.getLink()
        );
    }
}
