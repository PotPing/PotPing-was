package com.potping.domain.log.entity;

import com.potping.domain.pothole.entity.Pothole;
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
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DetectionLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pothole_id", nullable = false)
    private Pothole pothole;

    private String originalImgPath;  // 원본

    private String processedImgPath; // 보정본 (신호처리)

    private Double confidenceScore;  // 정확도

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public DetectionLog(Pothole pothole, String originalImgPath, String processedImgPath, Double confidenceScore) {
        this.pothole = pothole;
        this.originalImgPath = originalImgPath;
        this.processedImgPath = processedImgPath;
        this.confidenceScore = confidenceScore;
    }
}