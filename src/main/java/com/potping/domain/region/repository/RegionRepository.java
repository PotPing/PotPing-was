package com.potping.domain.region.repository;

import com.potping.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    // 최상위 지역 조회
    List<Region> findByParentIsNull();

    // 하위 지역 조회
    List<Region> findByParentId(Long parentId);
}
