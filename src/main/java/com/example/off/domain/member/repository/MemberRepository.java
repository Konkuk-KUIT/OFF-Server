package com.example.off.domain.member.repository;

import com.example.off.domain.member.Member;
import com.example.off.domain.role.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT DISTINCT m FROM Member m JOIN m.memberRoles mr WHERE mr.role = :role")
    List<Member> findAllByRole(@Param("role") Role role);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(@NotNull String email);
}
