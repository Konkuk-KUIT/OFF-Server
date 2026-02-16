package com.example.off.domain.pay.repository;

import com.example.off.domain.pay.PayLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayLogRepository extends JpaRepository<PayLog, Long> {
    Optional<PayLog> findByOrderId(String orderId);
}
