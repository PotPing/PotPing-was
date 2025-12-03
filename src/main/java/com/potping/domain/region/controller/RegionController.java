package com.potping.domain.region.controller;

import com.potping.domain.region.dto.request.RegionRequestDto;
import com.potping.domain.region.dto.response.RegionResponseDto;
import com.potping.domain.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Region API", description = "지역 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/region")
public class RegionController {
    private final RegionService regionService;

    @Operation(summary = "지역 목록 조회", description = "parentId가 없으면 도/광역시를, 있으면 시/군/구를 반환합니다.")
    @GetMapping
    public ResponseEntity<List<RegionResponseDto>> getRegions(
            @Parameter(description = "상위 지역 ID (선택)")
            @RequestParam(required = false) Long parentId) {

        List<RegionResponseDto> response = regionService.getRegions(parentId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "지역 추가", description = "새로운 지역을 등록합니다. 상위 지역 ID를 넣으면 하위 지역으로 등록됩니다.")
    @PostMapping
    public ResponseEntity<Long> createRegion(@RequestBody RegionRequestDto dto) {
        Long createdId = regionService.createRegion(dto);
        return ResponseEntity.ok(createdId);
    }
}
