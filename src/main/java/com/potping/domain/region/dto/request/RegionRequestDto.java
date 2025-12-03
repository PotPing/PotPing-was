package com.potping.domain.region.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegionRequestDto(
        @Schema(description = "지역명", example = "수성구")
        String name,

        @Schema(description = "상위 지역 ID (없으면 null)", example = "2")
        Long parentId
) {
}
