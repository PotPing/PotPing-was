package com.potping.domain.report.entity;

import com.potping.domain.pothole.entity.Pothole;
import com.potping.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportProcessStatus processStatus;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime reportedAt; // 신고 시각

    private LocalDateTime completedAt; // 보수 완료 시각

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pothole_id", nullable = false)
    private Pothole pothole; // 어떤 포트홀에 대한 신고인가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User admin; // 처리한 관리자

    @Builder
    public Report(Pothole pothole, User admin) {
        this.pothole = pothole;
        this.admin = admin;
        this.processStatus = ReportProcessStatus.SUBMITTED;
    }

    public void complete() {
        this.processStatus = ReportProcessStatus.DONE;
        this.completedAt = LocalDateTime.now();
    }

    // 완료 처리 시 담당자 배정 메서드
    public void assignAdmin(User admin) {
        this.admin = admin;
    }
}