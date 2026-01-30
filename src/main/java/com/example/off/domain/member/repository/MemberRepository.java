package com.example.off.domain.member.repository;

import com.example.off.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existByEmail(String email);
}
