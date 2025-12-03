package com.potping.domain.pothole.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DetectionRequestDto(
        @Schema(description = "세션 ID", example = "1")
        @JsonProperty("session_id")
        Long sessionId,

        @Schema(description = "영상 타임스탬프", example = "12.5")
        @JsonProperty("video_timestamp")
        Double videoTimestamp,

        @Schema(description = "심각도", example = "HIGH")
        @JsonProperty("severity")
        String severity,

        @Schema(description = "신뢰도", example = "0.85")
        @JsonProperty("average_confidence")
        Double confidence,

        @Schema(description = "상태", example = "DETECTED")
        @JsonProperty("status") // 파이썬이 보내주는 상태값 (보통 DETECTED)
        String status,

        @Schema(description = "이번 주행의 몇 번째 탐지인가", example = "5")
        @JsonProperty("total_detections") // [추가] 탐지 순번
        Integer sequenceNumber,

        @Schema(description = "포트홀 중심 좌표")
        @JsonProperty("detection_center")
        CenterCoordinate center,

        @JsonProperty("images")
        ImageNames images
) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CenterCoordinate(
                Double x,
                Double y
        ) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ImageNames(
                String original,
                String processed,
                String detected
        ) {}
}
