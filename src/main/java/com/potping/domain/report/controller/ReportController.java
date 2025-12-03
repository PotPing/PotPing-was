package com.potping.domain.report.controller;

import com.potping.domain.report.dto.response.ReportResponseDto;
import com.potping.domain.report.service.ReportService;
import com.potping.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Report API", description = "신고 접수 및 처리 관리")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 접수", description = "관리자가 포트홀을 확인하고 신고를 접수합니다.")
    @PostMapping("/{potholeId}")
    public ResponseEntity<ReportResponseDto> createReport(
            @PathVariable Long potholeId,
            @SessionAttribute(name = "LOGIN_USER", required = false) User admin) {

        if (admin == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        Long reportId = reportService.createReport(potholeId, admin.getId());
        ReportResponseDto response = reportService.getReportById(reportId); // (서비스에 추가 필요)

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "보수 완료 처리", description = "공사가 완료된 건을 처리 완료 상태로 변경합니다.")
    @PutMapping("/{reportId}/complete")
    public ResponseEntity<ReportResponseDto> completeReport(@PathVariable Long reportId) {
        reportService.completeReport(reportId);
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    @Operation(summary = "전체 신고 내역 조회", description = "시스템에 등록된 모든 신고 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}