package com.potping.domain.pothole.repository;

import com.potping.domain.pothole.entity.Pothole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PotholeRepository extends JpaRepository<Pothole, Long> {

    // 같은 세션에서 방금 전에 찍힌 포트홀이 있는지 확인
    @Query("SELECT p FROM Pothole p WHERE p.driveSession.id = :sessionId " +
            "AND p.videoTimestamp BETWEEN :startTime AND :endTime")
    Optional<Pothole> findDuplicate(@Param("sessionId") Long sessionId,
                                    @Param("startTime") int startTime,
                                    @Param("endTime") int endTime);

    // 특정 세션의 포트홀 총 개수 카운트
    long countByDriveSessionId(Long sessionId);
}
