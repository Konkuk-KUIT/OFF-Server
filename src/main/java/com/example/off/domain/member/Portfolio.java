package com.example.off.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_portfolio_member", columnList = "member_id")
        }
)
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String link;

    @Column(nullable = false, length = 1000)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Portfolio(String description, String link, Member member) {
        this.description = description;
        this.link = link;
        this.member = member;
    }

    public static Portfolio of(String description, String link, Member member){
        return new Portfolio(description, link, member);
    }

    public void setMember(Member member) {
        this.member = member;

        if (member != null && !member.getPortfolios().contains(this)) {
            member.getPortfolios().add(this);
        }
    }

}
