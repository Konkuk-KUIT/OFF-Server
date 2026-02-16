package com.example.off.domain.partnerRecruit.repository;

import com.example.off.domain.member.Member;
import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.partnerRecruit.PartnerRecruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerApplicationRepository extends JpaRepository<PartnerApplication, Long> {
    boolean existsByMemberAndPartnerRecruit(Member member, PartnerRecruit partnerRecruit);
}
