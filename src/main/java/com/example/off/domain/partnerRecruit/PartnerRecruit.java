package com.example.off.domain.partnerRecruit;

import com.example.off.domain.project.Project;
import com.example.off.domain.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "partner_recruit",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_role",
                        columnNames = {"project_id", "role"}
                )
        },
        indexes = {
                @Index(name = "idx_status_role", columnList = "recruitStatus, role"),
        }
)
public class PartnerRecruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_recruit_id")
    private Long id;

    @Column(nullable = false)
    private Integer numberOfPerson;

    @Column(nullable = false)
    private Integer cost;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitStatus recruitStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "partnerRecruit")
    private List<PartnerApplication> partnerApplications = new ArrayList<>();

    public PartnerRecruit(Project project, Role role, Integer numberOfPerson, RecruitStatus recruitStatus) {
        this.project = project;
        this.role = role;
        this.numberOfPerson = numberOfPerson;
        this.recruitStatus = recruitStatus;
    }
}
