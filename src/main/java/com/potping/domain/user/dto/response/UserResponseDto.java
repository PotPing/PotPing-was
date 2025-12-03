package com.potping.domain.user.dto.response;

import com.potping.domain.user.entity.Role;
import com.potping.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(
        @Schema(description = "유저 고유 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 아이디", example = "admin")
        String username,

        @Schema(description = "역할", example = "ADMIN")
        Role role
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
