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
     * 신고 접수 기능
     * @param potholeId 신고 대상 포트홀 ID
     * @param adminId   신고를 승인한 관리자 ID (실명제)
     * @return 생성된 신고 내역의 ID (PK)
     * @throws IllegalArgumentException 포트홀이나 관리자 정보가 없을 경우
     * @throws IllegalStateException    이미 신고된 포트홀일 경우 (중복 방지)
     */
    public Long createReport(Long potholeId, Long adminId) {
        // 포트홀 조회
        Pothole pothole = potholeRepository.findById(potholeId)
                .orElseThrow(() -> new IllegalArgumentException("포트홀을 찾을 수 없습니다."));

        // 이미 신고된 건인지 확인
        if (reportRepository.findByPotholeId(potholeId).isPresent()) {
            throw new IllegalStateException("이미 신고 접수된 건입니다.");
        }

        // 관리자 조회
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보를 찾을 수 없습니다."));

        // 리포트 생성
        Report report = Report.builder()
                .pothole(pothole)
                .admin(admin)
                .build();
        reportRepository.save(report);

        // 포트홀 상태 변경 (탐지 -> 신고)
        pothole.changeStatus(PotholeStatus.REPORTED);

        return report.getId();
    }

    /**
     * 보수 완료 처리 기능
     * @param reportId 처리할 신고 내역 ID
     * @throws IllegalArgumentException 신고 내역을 찾을 수 없을 경우
     */
    public void completeReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

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
}