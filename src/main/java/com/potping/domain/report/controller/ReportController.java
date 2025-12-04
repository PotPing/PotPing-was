package com.potping.domain.report.controller;

import com.potping.domain.report.dto.response.ReportResponseDto;
import com.potping.domain.report.service.ReportService;
import com.potping.domain.user.entity.Role;
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

    @Operation(summary = "보수 완료 처리", description = "공사가 완료된 건을 처리 완료 상태로 변경합니다.")
    @PostMapping
    public ResponseEntity<String> completeReport(
            @PathVariable Long reportId,
            @SessionAttribute(name = "LOGIN_USER", required = false) User admin) {

        if (admin == null) throw new IllegalArgumentException("로그인이 필요합니다.");

        reportService.completeReport(reportId, admin.getId());

        return ResponseEntity.ok("보수 완료 처리되었습니다.");
    }

    @Operation(summary = "전체 신고 내역 조회", description = "시스템에 등록된 모든 신고 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @Operation(summary = "내 신고/처리 내역 조회", description = "관리자는 '처리한 내역'을, 운전자는 '발견해서 신고된 내역'을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<ReportResponseDto>> getMyReports(
            @SessionAttribute(name = "LOGIN_USER", required = false) User loginUser) {

        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        List<ReportResponseDto> response;

        if (loginUser.getRole() == Role.ADMIN) {
            // 관리자라면 -> 내가 처리(보수완료)한 목록
            response = reportService.getReportsByAdmin(loginUser.getId());
        } else {
            // 운전자라면 -> 내가 주행 중 발견한 목록
            response = reportService.getReportsByUser(loginUser.getId());
        }

        return ResponseEntity.ok(response);
    }
}