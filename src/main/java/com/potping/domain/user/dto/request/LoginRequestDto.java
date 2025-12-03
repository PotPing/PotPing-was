package com.potping.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDto(
        @Schema(description = "로그인 아이디", example = "user")
        String username,

        @Schema(description = "비밀번호", example = "abcd1234")
        String password
) {
}
