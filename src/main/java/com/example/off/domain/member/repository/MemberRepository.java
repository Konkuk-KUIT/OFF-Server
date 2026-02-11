package com.example.off.domain.member.repository;

import com.example.off.domain.member.Member;
import com.example.off.domain.role.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByRole(Role role);

    Page<Member> findAllByRoleIn(Collection<Role> roles, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(@NotNull String email);
}