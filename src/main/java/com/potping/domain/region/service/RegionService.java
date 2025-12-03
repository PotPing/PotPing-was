package com.potping.domain.region.service;

import com.potping.domain.region.dto.request.RegionRequestDto;
import com.potping.domain.region.dto.response.RegionResponseDto;
import com.potping.domain.region.entity.Region;
import com.potping.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    /**
     * 계층형 지역 목록 조회 기능
     *
     * @param parentId 상위 지역 ID (null이면 도/광역시, 값이 있으면 시/군/구 조회)
     * @return 해당 조건에 맞는 지역 목록(DTO)
     */
    public List<RegionResponseDto> getRegions(Long parentId) {
        List<Region> regions;

        if (parentId == null) {
            // 부모가 없는 최상위 지역 조회 (예: 경상북도, 대구광역시)
            regions = regionRepository.findByParentIsNull();
        } else {
            // 특정 지역의 하위 지역 조회 (예: 경북(1) -> 경산시, 구미시)
            regions = regionRepository.findByParentId(parentId);
        }
        return regions.stream()
                .map(RegionResponseDto::from)
                .toList();
    }

    /**
     * 지역 추가 기능
     *
     * @param dto 추가할 지역 정보 (이름, 부모 ID, 담당자 ID)
     * @return 생성된 지역의 ID (PK)
     * @throws IllegalArgumentException 부모 지역 ID가 유효하지 않을 경우 예외 발생
     */
    @Transactional
    public Long createRegion(RegionRequestDto dto) {
        Region parent = null;

        // 부모 지역이 지정되었다면 DB에서 조회
        if (dto.parentId() != null) {
            parent = regionRepository.findById(dto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 지역을 찾을 수 없습니다."));
        }

        // 엔티티 생성 (부모가 null이면 최상위 지역이 됨)
        Region region = Region.builder()
                .name(dto.name())
                .parent(parent)
                .build();

        regionRepository.save(region);

        return region.getId();
    }
}
