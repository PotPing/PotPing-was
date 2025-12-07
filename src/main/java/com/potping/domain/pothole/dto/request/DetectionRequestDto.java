package com.potping.domain.pothole.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DetectionRequestDto(
        @Schema(description = "주행 세션 ID", example = "55")
        @JsonProperty("session_id")
        Long sessionId,

        @Schema(description = "영상 타임스탬프", example = "12.5")
        @JsonProperty("video_timestamp")
        Double videoTimestamp,

        @Schema(description = "심각도", example = "HIGH")
        @JsonProperty("severity")
        String severity,

        @Schema(description = "상태 (DETECTED 등)", example = "DETECTED")
        @JsonProperty("status")
        String status,

        @Schema(description = "감지된 총 개수", example = "5")
        @JsonProperty("total_detections")
        Integer totalDetections,

        @Schema(description = "평균 신뢰도", example = "0.85")
        @JsonProperty("average_confidence")
        Double averageConfidence,

        @JsonProperty("detection_center")
        CenterCoordinate center,

        @Schema(description = "이미지 파일 이름들")
        @JsonProperty("images")
        ImageNames images
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImageNames(
            String original,
            String processed,
            String detected
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CenterCoordinate(
            Double x,
            Double y
    ) {}
}
