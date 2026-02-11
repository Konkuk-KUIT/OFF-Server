package com.example.off.domain.pay;

import com.example.off.domain.member.Member;
import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.projectMember.ProjectMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "pay_log",
        indexes = {
                @Index(name = "idx_payer_created", columnList = "member_id, createdAt DESC"),
                @Index(name = "idx_payee_created", columnList = "project_member_id, createdAt DESC")
        }
)
public class PayLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_log_id")
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayStatus status;

    @Column
    private LocalDateTime paidAt;

    @Column(nullable = false, unique = true, length = 64)
    private String orderId;

    @Column(length = 200)
    private String paymentKey; // confirm 성공 후 저장

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_member_id")
    private ProjectMember projectMember;

    // 어떤 지원서 결제인지(prepare 단계에서 연결해둬야 confirm에서 찾기 쉬움)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private PartnerApplication application;

    private PayLog(Long amount, PayStatus status, Member member, ProjectMember projectMember) {
        this.amount = amount;
        this.status = status;
        this.payer = member;
        this.projectMember = projectMember;
    }

    private PayLog(String orderId, Long amount, PayStatus status, Member payer, PartnerApplication application) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.payer = payer;
        this.application = application;
    }

    public static PayLog of(Long amount, PayStatus status, Member member, ProjectMember projectMember){
        return new PayLog(amount, status, member, projectMember);
    }

    public static PayLog ready(String orderId, Long amount, Member payer, PartnerApplication application) {
        return new PayLog(orderId, amount, PayStatus.READY, payer, application);
    }

    public void markPaid(String paymentKey, ProjectMember projectMember) {
        this.paymentKey = paymentKey;
        this.projectMember = projectMember;
        this.status = PayStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = PayStatus.FAILED;
    }

}
