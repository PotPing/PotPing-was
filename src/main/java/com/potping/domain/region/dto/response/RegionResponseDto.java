package com.potping.domain.region.dto.response;

import com.potping.domain.region.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegionResponseDto(
        @Schema(description = "지역 ID")
        Long id,

        @Schema(description = "지역명", example = "경상북도")
        String name
) {
    public static RegionResponseDto from(Region region) {
        return new RegionResponseDto(region.getId(), region.getName());
    }
}
