package com.potping.domain.report.repository;

import com.potping.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 특정 포트홀에 대한 신고 내역이 있는지 조회 (중복 신고 방지용)
    Optional<Report> findByPotholeId(Long potholeId);

    // 특정 관리자가 처리한 신고 내역 조회
    List<Report> findByAdminId(Long adminId);

    // "내 주행 세션에서 발생한 신고 내역" 조회
    List<Report> findByPothole_DriveSession_User_Id(Long userId);
}