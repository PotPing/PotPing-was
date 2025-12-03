package com.potping.domain.report.repository;

import com.potping.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 특정 포트홀에 대한 신고 내역이 있는지 조회 (중복 신고 방지용)
    Optional<Report> findByPotholeId(Long potholeId);
}