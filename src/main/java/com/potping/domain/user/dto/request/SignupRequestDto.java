package com.potping.domain.user.dto.request;

import com.potping.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequestDto(
        @Schema(description = "사용자 아이디", example = "user")
        String username,

        @Schema(description = "비밀번호", example = "abcd1234")
        String password,

        @Schema(description = "역할 (DRIVER 또는 ADMIN)", example = "DRIVER")
        Role role
) {}
