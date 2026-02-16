package com.example.off.domain.project;

import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.member.Member;
import com.example.off.domain.partnerRecruit.PartnerRecruit;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "project",
        indexes = {
                @Index(name = "idx_project_type_start", columnList = "projectType, start_date"),
                @Index(name = "idx_project_type_end", columnList = "projectType, end_date"),
                @Index(name = "idx_project_start", columnList = "start_date"),
                @Index(name = "idx_project_name", columnList = "name"),
                @Index(name = "idx_project_creator", columnList = "creator_id")
        }
)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 2000)
    private String requirement;

    @Column(nullable = false)
    private Long estimation;

    @Column(name = "start_date", nullable = false)
    private LocalDate start;

    @Column(name = "end_date", nullable = false)
    private LocalDate end;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<PartnerRecruit> partnerRecruits = new ArrayList<>();

    @Column(length = 200)
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    public Project(String name, String description, String requirement, Long estimation,
                   LocalDate start, LocalDate end, ProjectType projectType, Member creator) {
        this.name = name;
        this.description = description;
        this.requirement = requirement;
        this.estimation = estimation;
        this.start = start;
        this.end = end;
        this.projectType = projectType;
        this.creator = creator;
        this.status = ProjectStatus.IN_PROGRESS;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void complete() {
        this.status = ProjectStatus.COMPLETED;
    }
}
