package com.potping.domain.report.repository;

import com.potping.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 세션 ID로 신고 내역 찾기 (중복 신고 방지)
    Optional<Report> findByDriveSessionId(Long sessionId);

    // 특정 관리자가 처리한 신고 내역 조회
    List<Report> findByAdminId(Long adminId);

    // 운전자별 신고 내역 조회 (세션의 운전자로 찾기)
    List<Report> findByDriveSession_User_Id(Long userId);
}