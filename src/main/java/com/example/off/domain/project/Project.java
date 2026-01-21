package com.example.off.domain.project;

import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "project",
        indexes = {
                @Index(name = "idx_project_type_start", columnList = "projectType, start"),
                @Index(name = "idx_project_type_end", columnList = "projectType, end"),
                @Index(name = "idx_project_start", columnList = "start"),
                @Index(name = "idx_project_name", columnList = "name")
        }
)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String requirement;

    @Column(nullable = false)
    private Long estimation;

    @Column(nullable = false)
    private LocalDate start;

    @Column(nullable = false)
    private LocalDate end;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ChatRoom> chatRooms = new ArrayList<>();
}
