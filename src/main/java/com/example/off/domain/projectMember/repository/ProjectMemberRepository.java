package com.example.off.domain.projectMember.repository;

import com.example.off.domain.projectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByMember_Id(Long memberId);
}
