package com.potping.domain.pothole.dto.response;

import com.potping.domain.log.entity.DetectionLog;
import com.potping.domain.pothole.entity.Pothole;
import com.potping.domain.pothole.entity.PotholeSeverity;
import com.potping.domain.pothole.entity.PotholeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PotholeResponseDto(
        @Schema(description = "포트홀 ID", example = "1")
        Long potholeId,

        @Schema(description = "발견된 주행 세션 ID", example = "55")
        Long sessionId,

        @Schema(description = "영상 내 발생 시간(초)", example = "12")
        Integer videoTimestamp,

        @Schema(description = "심각도", example = "HIGH")
        PotholeSeverity severity,

        @Schema(description = "처리 상태", example = "DETECTED")
        PotholeStatus status,

        @Schema(description = "발견 시각", example = "2023-12-04T10:00:00")
        LocalDateTime detectedAt,

        @Schema(description = "원본 이미지 경로")
        String originalImg,

        @Schema(description = "신호처리된 이미지 경로")
        String processedImg
) {
    public static PotholeResponseDto from(Pothole pothole, DetectionLog log) {
        return new PotholeResponseDto(
                pothole.getId(),
                pothole.getDriveSession().getId(),
                pothole.getVideoTimestamp(),
                pothole.getSeverity(),
                pothole.getStatus(),
                pothole.getCreatedAt(),
                log != null ? log.getOriginalImgPath() : null,
                log != null ? log.getProcessedImgPath() : null
        );
    }
}
