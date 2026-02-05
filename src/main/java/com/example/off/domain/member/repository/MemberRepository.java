package com.example.off.domain.member.repository;

import com.example.off.domain.member.Member;
import com.example.off.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT DISTINCT m FROM Member m JOIN m.memberRoles mr WHERE mr.role = :role")
    List<Member> findAllByRole(@Param("role") Role role);
}
