package com.potping.domain.session.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SessionRequestDto(
        @Schema(description = "운전자(user) ID", example = "1")
        Long userId,

        @Schema(description = "선택한 하위 지역(region) ID", example = "1")
        Long regionId
) {
}
