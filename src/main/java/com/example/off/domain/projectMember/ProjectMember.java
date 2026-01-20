package com.example.off.domain.projectMember;

import com.example.off.domain.member.Member;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.project.Project;
import com.example.off.domain.role.Role;
import com.example.off.domain.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Column(nullable = false)
    private Boolean isPartner;

    @Column(nullable = false)
    private Role role;

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
}
