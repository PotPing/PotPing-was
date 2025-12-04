package com.potping.domain.report.service;

import com.potping.domain.pothole.entity.Pothole;
import com.potping.domain.pothole.entity.PotholeStatus;
import com.potping.domain.pothole.repository.PotholeRepository;
import com.potping.domain.report.dto.response.ReportResponseDto;
import com.potping.domain.report.entity.Report;
import com.potping.domain.report.repository.ReportRepository;
import com.potping.domain.session.entity.DriveSession;
import com.potping.domain.session.repository.DriveSessionRepository;
import com.potping.domain.user.entity.User;
import com.potping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final PotholeRepository potholeRepository;
    private final DriveSessionRepository driveSessionRepository;
    private final UserRepository userRepository;

    /**
     * 자동 신고 접수 기능 (시스템에 의해 호출)
     * @param sessionId 신고 대상 포트홀 ID
     * @return 생성된 신고 내역의 ID (PK)
     */
    public Long createReportForSession(Long sessionId) {
        DriveSession session = driveSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        // 이미 신고된 세션인지 확인
        if (reportRepository.findByDriveSessionId(sessionId).isPresent()) {
            return null;
        }

        // 1. 리포트 생성
        Report report = Report.builder()
                .driveSession(session)
                .admin(null)
                .build();
        reportRepository.save(report);

        // 2. 해당 세션의 모든 포트홀 상태: DETECTED -> REPORTED
        List<Pothole> potholes = potholeRepository.findAll().stream()
                .filter(p -> p.getDriveSession().getId().equals(sessionId))
                .toList();

        for (Pothole p : potholes) {
            p.changeStatus(PotholeStatus.REPORTED);
        }

        return report.getId();
    }

    /**
     * 보수 완료 처리 기능
     * @param reportId 처리할 신고 내역 ID
     * @param adminId 처리한 관리자 ID
     * @throws IllegalArgumentException 신고 내역을 찾을 수 없을 경우
     * @throws IllegalArgumentException 관리자를 찾을 수 없는 경우
     */
    public void completeReport(Long reportId, Long adminId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다."));

        // 1. 리포트 완료 처리 (담당자 기록)
        report.assignAdmin(admin);
        report.complete();

        // 2. 해당 세션의 모든 포트홀 상태: REPORTED -> FIXED
        List<Pothole> potholes = potholeRepository.findAll().stream()
                .filter(p -> p.getDriveSession().getId().equals(report.getDriveSession().getId()))
                .toList();

        for (Pothole p : potholes) {
            p.changeStatus(PotholeStatus.FIXED);
        }
    }

    /**
     * 신고 내역 단건 조회
     * @param reportId 조회할 신고 ID
     * @return 신고 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public ReportResponseDto getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        // 해당 포트홀이 속한 세션의 전체 포트홀 개수 조회
        Long count = potholeRepository.countByDriveSessionId(report.getDriveSession().getId());

        return ReportResponseDto.from(report, count);
    }

    /**
     * 전체 신고 내역 조회 (관리자용)
     * @return 시스템에 등록된 모든 신고 내역 리스트
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(report -> {
                    Long count = potholeRepository.countByDriveSessionId(report.getDriveSession().getId());
                    return ReportResponseDto.from(report, count);
                })
                .toList();
    }

    /**
     * 특정 관리자의 처리 내역 조회
     * @param adminId 조회할 관리자 ID
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByAdmin(Long adminId) {
        return reportRepository.findByAdminId(adminId).stream()
                .map(report -> {
                    Long count = potholeRepository.countByDriveSessionId(report.getDriveSession().getId());
                    return ReportResponseDto.from(report, count);
                })
                .toList();
    }

    /**
     * 운전자 본인의 신고 내역 조회 (내가 발견한 것들)
     * @param userId 조회할 운전자 ID
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByUser(Long userId) {
        return reportRepository.findByDriveSession_User_Id(userId).stream()
                .map(report -> {
                    // 해당 포트홀이 속한 세션의 총 포트홀 개수 조회
                    Long count = potholeRepository.countByDriveSessionId(report.getDriveSession().getId());
                    return ReportResponseDto.from(report, count);
                })
                .toList();
    }
}