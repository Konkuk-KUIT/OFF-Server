package com.example.off.domain.projectMember;

import com.example.off.domain.member.Member;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.project.Project;
import com.example.off.domain.role.Role;
import com.example.off.domain.task.Task;
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
        name = "project_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_member_project",
                        columnNames = {"member_id", "project_id"}
                )
        },
        indexes = {
                @Index(name = "idx_project_role", columnList = "project_id, role"),
        }
)
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "projectMember")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "projectMember")
    private List<PayLog> payLogs = new ArrayList<>();

    private ProjectMember(Member member, Project project) {
        this.member = member;
        this.project = project;
        this.role = member.getRole();
    }

    public static ProjectMember of(Project project, Member member) {
       return new ProjectMember(member, project);
    }
}
