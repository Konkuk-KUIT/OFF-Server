package com.example.off.domain.projectMember.repository;

import com.example.off.domain.projectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByMember_Id(Long memberId);
    @Query("""
            select case when count(pm) > 0 then true else false and
            from ProjectMember pm
            where pm.member_id = :memberId
                and pm.project.endDate > :now
            """)
    boolean existsWorkingProject(Long memberId, LocalDateTime now);

    @Query("""
    select pm
    from ProjectMember pm
    where pm.member.id = :memberId
      and pm.project.endDate > :now
    order by pm.project.endDate asc
""")
    Optional<ProjectMember> findWorkingProject(Long memberId, LocalDateTime now);
}
