package com.potping.domain.pothole.entity;

import com.potping.domain.session.entity.DriveSession;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Pothole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pothole_id")
    private Long id;

    @Column(name = "video_timestamp", nullable = false)
    private Integer videoTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PotholeSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PotholeStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private DriveSession driveSession;

    @Builder
    public Pothole(DriveSession driveSession, Integer videoTimestamp, PotholeSeverity severity, PotholeStatus status) {
        this.driveSession = driveSession;
        this.videoTimestamp = videoTimestamp;
        this.severity = severity != null ? severity : PotholeSeverity.MEDIUM;
        this.status = status != null ? status : PotholeStatus.DETECTED;
    }

    // 상태 변경 메서드 (신고 접수/완료 시 사용)
    public void changeStatus(PotholeStatus newStatus) {
        this.status = newStatus;
    }
}
