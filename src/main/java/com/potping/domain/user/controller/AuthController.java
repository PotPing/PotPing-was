package com.potping.domain.user.controller;

import com.potping.domain.user.dto.request.LoginRequestDto;
import com.potping.domain.user.dto.request.SignupRequestDto;
import com.potping.domain.user.dto.response.UserResponseDto;
import com.potping.domain.user.entity.User;
import com.potping.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "회원가입, 로그인, 로그아웃 기능")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "신규 관리자 또는 운전자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하고 세션을 생성합니다.")
    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto dto, HttpServletRequest request) {

        // 1. 서비스에서 유저 검증
        User user = authService.login(dto.username(), dto.password());

        // 2. 세션 생성 (없으면 새로 만듦)
        HttpSession session = request.getSession(true);

        // 3. 세션에 회원 정보 저장
        session.setAttribute("LOGIN_USER", user);

        // 4. 응답 DTO 반환
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
