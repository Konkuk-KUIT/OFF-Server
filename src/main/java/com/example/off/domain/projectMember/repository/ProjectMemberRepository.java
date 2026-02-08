package com.example.off.domain.projectMember.repository;

import com.example.off.domain.projectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByMember_Id(Long memberId);

    @Query("""
                select pm
                from ProjectMember pm
                where pm.member.id = :memberId
                  and pm.project.end > :now
                order by pm.project.end asc
            """)
    Optional<ProjectMember> findWorkingProject(
            @Param("memberId") Long memberId,
            @Param("now") LocalDateTime now);
}
