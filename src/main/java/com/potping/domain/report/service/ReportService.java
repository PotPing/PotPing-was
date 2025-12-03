package com.potping.domain.report.service;

import com.potping.domain.pothole.entity.Pothole;
import com.potping.domain.pothole.entity.PotholeStatus;
import com.potping.domain.pothole.repository.PotholeRepository;
import com.potping.domain.report.dto.response.ReportResponseDto;
import com.potping.domain.report.entity.Report;
import com.potping.domain.report.repository.ReportRepository;
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
    private final UserRepository userRepository;

    /**
     * 자동 신고 접수 기능 (시스템에 의해 호출)
     * @param potholeId 신고 대상 포트홀 ID
     * @return 생성된 신고 내역의 ID (PK)
     */
    public Long createReport(Long potholeId) {
        Pothole pothole = potholeRepository.findById(potholeId)
                .orElseThrow(() -> new IllegalArgumentException("포트홀을 찾을 수 없습니다."));

        // 중복 신고 방지
        if (reportRepository.findByPotholeId(potholeId).isPresent()) {
            return null; // 이미 신고된 건이면 패스 (또는 예외 처리)
        }

        // 관리자 없이 리포트 생성
        Report report = Report.builder()
                .pothole(pothole)
                .admin(null) // 아직 담당자 없음
                .build();

        reportRepository.save(report);

        // 포트홀 상태 변경 (DETECTED -> REPORTED)
        pothole.changeStatus(PotholeStatus.REPORTED);

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
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보를 찾을 수 없습니다."));

        // 처리한 관리자 정보 업데이트
        report.assignAdmin(admin);

        // 신고 상태 변경
        report.complete();

        // 포트홀 상태 변경 (신고 -> 보수 완료)
        report.getPothole().changeStatus(PotholeStatus.FIXED);
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
        return ReportResponseDto.from(report);
    }

    /**
     * 전체 신고 내역 조회 (관리자용)
     * @return 시스템에 등록된 모든 신고 내역 리스트
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(ReportResponseDto::from)
                .toList();
    }

    /**
     * 특정 관리자의 처리 내역 조회
     * @param adminId 조회할 관리자 ID
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByAdmin(Long adminId) {
        return reportRepository.findByAdminId(adminId).stream()
                .map(ReportResponseDto::from)
                .toList();
    }

    /**
     * 운전자 본인의 신고 내역 조회 (내가 발견한 것들)
     * @param userId 조회할 운전자 ID
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByUser(Long userId) {
        return reportRepository.findByPothole_DriveSession_User_Id(userId).stream()
                .map(ReportResponseDto::from)
                .toList();
    }
}