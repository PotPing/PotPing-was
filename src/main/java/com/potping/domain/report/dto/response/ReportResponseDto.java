package com.potping.domain.report.dto.response;

import com.potping.domain.report.entity.Report;
import com.potping.domain.report.entity.ReportProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReportResponseDto(
        @Schema(description = "신고 ID", example = "1")
        Long reportId,

        @Schema(description = "관련 포트홀 ID", example = "55")
        Long potholeId,

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
        return new ReportResponseDto(
                report.getId(),
                report.getPothole().getId(),
                report.getPothole().getDriveSession().getRegion().getName(),
                totalPotholes,
                report.getAdmin().getUsername(),
                report.getProcessStatus(),
                report.getReportedAt(),
                report.getCompletedAt()
        );
    }
}