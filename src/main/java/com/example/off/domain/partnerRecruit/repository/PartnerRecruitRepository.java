package com.example.off.domain.partnerRecruit.repository;

import com.example.off.domain.partnerRecruit.PartnerRecruit;
import com.example.off.domain.project.Project;
import com.example.off.domain.role.Role;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartnerRecruitRepository extends JpaRepository<PartnerRecruit, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pr FROM PartnerRecruit pr WHERE pr.id = :id")
    Optional<PartnerRecruit> findByIdForUpdate(@Param("id") Long id);

    Optional<PartnerRecruit> findByProjectAndRole(Project project, Role role);

    List<PartnerRecruit> findAllByProject_Id(Long projectId);

    List<PartnerRecruit> findAllByProjectAndRecruitStatus(Project project, com.example.off.domain.partnerRecruit.RecruitStatus recruitStatus);
}