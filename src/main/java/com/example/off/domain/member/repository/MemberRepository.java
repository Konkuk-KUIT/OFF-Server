package com.example.off.domain.member.repository;

import com.example.off.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existByEmail(String email);
    Optional<Member> findByEmail(@NotNull String email);
}
