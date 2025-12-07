package com.potping.domain.report.dto.response;

import com.potping.domain.report.entity.Report;
import com.potping.domain.report.entity.ReportProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReportResponseDto(
        @Schema(description = "신고 ID", example = "1")
        Long reportId,

        @Schema(description = "관련 주행 ID", example = "55")
        Long sessionId,

        @Schema(description = "발견 지역명", example = "경상북도 경산시")
        String regionName,

        @Schema(description = "해당 세션의 포트홀 총 개수", example = "12")
        Long totalPotholesInSession,

        @Schema(description = "처리한 관리자 이름", example = "admin")
        String adminName,

        @Schema(description = "진행 상태 (SUBMITTED, DONE)", example = "SUBMITTED")
        ReportProcessStatus processStatus,

        @Schema(description = "신고 접수 시각")
        LocalDateTime reportedAt,

        @Schema(description = "보수 완료 시각 (완료 전엔 null)")
        LocalDateTime completedAt
) {

    public static ReportResponseDto from(Report report, Long totalPotholes) {

        // 세션 / 지역 정보 안전하게 추출
        Long sessionId = null;
        String regionName = null;

        if (report.getDriveSession() != null) {
            sessionId = report.getDriveSession().getId();

            var region = report.getDriveSession().getRegion();
            if (region != null) {
                if (region.getParent() != null) {
                    // 상위 지역 + 하위 지역 (예: 경상북도 경산시)
                    regionName = region.getParent().getName() + " " + region.getName();
                } else {
                    regionName = region.getName();
                }
            }
        }

        // 관리자 이름 (담당자 미지정일 수 있음)
        String adminName = null;
        if (report.getAdmin() != null) {
            adminName = report.getAdmin().getUsername();
        }

        return new ReportResponseDto(
                report.getId(),
                sessionId,
                regionName,
                totalPotholes,
                adminName,
                report.getProcessStatus(),
                report.getReportedAt(),
                report.getCompletedAt()
        );
    }
}
