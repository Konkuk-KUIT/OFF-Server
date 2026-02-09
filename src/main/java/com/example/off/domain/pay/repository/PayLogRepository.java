package com.example.off.domain.pay.repository;

import com.example.off.domain.pay.PayLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayLogRepository extends JpaRepository<PayLog, Long> {
}
