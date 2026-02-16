package com.example.off.domain.task;

import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.ProjectMember;
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
        indexes = {
                @Index(name = "idx_task_project", columnList = "project_id"),
                @Index(name = "idx_task_project_member", columnList = "project_member_id")
        }
)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_member_id", nullable = false)
    private ProjectMember projectMember;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDo> toDoList = new ArrayList<>();

    private Task(String name, String description, Project project, ProjectMember projectMember) {
        this.name = name;
        this.description = description;
        this.project = project;
        this.projectMember = projectMember;
    }

    public static Task of(String name, String description, Project project, ProjectMember projectMember) {
        return new Task(name, description, project, projectMember);
    }

    public void update(String name, String description, ProjectMember projectMember) {
        this.name = name;
        this.description = description;
        this.projectMember = projectMember;
    }
}
