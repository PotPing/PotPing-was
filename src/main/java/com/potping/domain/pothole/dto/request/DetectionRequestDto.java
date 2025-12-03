package com.potping.domain.pothole.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

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

        @JsonProperty("images")
        ImageNames images
) {
    public record ImageNames(
            String original,
            String processed,
            String detected
    ) {}
}
