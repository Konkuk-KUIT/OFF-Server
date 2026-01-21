package com.example.off.domain.partnerRecruit;

import com.example.off.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "partner_application",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_recruit", columnNames = {"member_id", "partner_recruit_id"})
        },
        indexes = {
                @Index(name = "idx_member_type_status", columnList = "member_id, isFromProject, applicationStatus"),
                @Index(name = "idx_recruit_type_status", columnList = "partner_recruit_id, isFromProject, applicationStatus")
        }
)
public class PartnerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Column(nullable = false)
    private Boolean isFromProject;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_recruit_id", nullable = false)
    private PartnerRecruit partnerRecruit;
}
