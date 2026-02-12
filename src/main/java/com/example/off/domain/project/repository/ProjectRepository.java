package com.example.off.domain.project.repository;

import com.example.off.domain.project.Project;
import com.example.off.domain.project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByProjectMembers_Member_IdAndStatus(Long memberId, ProjectStatus status);
    List<Project> findAllByStatus(ProjectStatus status);
    List<Project> findAllByCreator_IdAndStatus(Long creatorId, ProjectStatus status);
    boolean existsByCreator_IdAndStatusAndIdNot(Long creatorId, ProjectStatus status, Long projectId);
}