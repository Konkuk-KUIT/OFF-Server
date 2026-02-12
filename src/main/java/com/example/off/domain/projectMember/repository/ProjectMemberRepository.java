package com.example.off.domain.projectMember.repository;

import com.example.off.domain.member.Member;
import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByMember_Id(Long memberId);
    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);
    boolean existsByProjectAndMember(Project project, Member member);

    @Query("""
                select pm
                from ProjectMember pm
                where pm.member.id = :memberId
                  and pm.project.end > :now
                  and pm.project.status = 'IN_PROGRESS'
                order by pm.project.end asc
            """)
    Optional<ProjectMember> findWorkingProject(
            @Param("memberId") Long memberId,
            @Param("now") LocalDate now);
}
