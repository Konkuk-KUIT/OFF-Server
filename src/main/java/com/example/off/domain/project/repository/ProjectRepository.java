package com.example.off.domain.project.repository;

import com.example.off.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}