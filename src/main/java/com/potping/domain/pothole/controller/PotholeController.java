package com.potping.domain.pothole.controller;

import com.potping.domain.pothole.dto.request.DetectionRequestDto;
import com.potping.domain.pothole.dto.response.PotholeResponseDto;
import com.potping.domain.pothole.service.PotholeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pothole API", description = "포트홀 탐지 정보 수신")
@RestController
@RequestMapping("/api/pothole")
@RequiredArgsConstructor
public class PotholeController {

    private final PotholeService potholeService;

    @Operation(summary = "탐지 정보 수신", description = "YOLO로부터 포트홀 탐지 데이터를 수신하여 저장합니다.")
    @PostMapping("/detection")
    public ResponseEntity<String> receiveDetection(@RequestBody DetectionRequestDto dto) {
        potholeService.processDetection(dto);
        return ResponseEntity.ok("탐지 정보 저장 완료");
    }

    @Operation(summary = "세션별 포트홀 조회", description = "특정 주행 세션에서 발견된 포트홀 목록을 반환합니다.")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<PotholeResponseDto>> getPotholesBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(potholeService.getPotholesBySession(sessionId));
    }
}