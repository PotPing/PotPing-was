package com.potping.domain.session.controller;

import com.potping.domain.session.dto.request.SessionRequestDto;
import com.potping.domain.session.service.DriveSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Session API", description = "주행 세션 관리")
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {
    private final DriveSessionService driveSessionService;

    @Operation(summary = "주행 시작", description = "지역을 선택하고 주행을 시작합니다. (세션 ID 발급)")
    @PostMapping("/start")
    public ResponseEntity<Long> startSession(@RequestBody SessionRequestDto dto) {
        return ResponseEntity.ok(driveSessionService.startSession(dto));
    }

    @Operation(summary = "주행 종료", description = "주행을 종료하고 시간을 기록합니다.")
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<String> endSession(@PathVariable Long sessionId) {
        driveSessionService.endSession(sessionId);
        return ResponseEntity.ok("주행이 종료되었습니다.");
    }
}
