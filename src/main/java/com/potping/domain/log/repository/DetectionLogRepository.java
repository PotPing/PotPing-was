package com.potping.domain.log.repository;

import com.potping.domain.log.entity.DetectionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetectionLogRepository extends JpaRepository<DetectionLog, Long> {
    // 특정 포트홀의 로그 목록 조회
    List<DetectionLog> findByPotholeId(Long potholeId);
}