package com.potping.domain.session.entity;

import com.potping.domain.region.entity.Region;
import com.potping.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AbstractMethodError.class)
public class DriveSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 운전자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region; // 선택한 주행 지역

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt; // 주행 시작 시간

    private LocalDateTime endedAt;   // 주행 종료 시간

    @Builder
    public DriveSession(User user, Region region) {
        this.user = user;
        this.region = region;
    }

    // 주행 종료 시 시간 업데이트
    public void endSession() {
        this.endedAt = LocalDateTime.now();
    }
}
