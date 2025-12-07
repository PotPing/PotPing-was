package com.potping.domain.pothole.service;

import com.potping.domain.log.entity.DetectionLog;
import com.potping.domain.log.repository.DetectionLogRepository;
import com.potping.domain.pothole.dto.request.DetectionRequestDto;
import com.potping.domain.pothole.dto.response.PotholeResponseDto;
import com.potping.domain.pothole.entity.Pothole;
import com.potping.domain.pothole.entity.PotholeSeverity;
import com.potping.domain.pothole.repository.PotholeRepository;
import com.potping.domain.session.entity.DriveSession;
import com.potping.domain.session.repository.DriveSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PotholeService {
    private final PotholeRepository potholeRepository;
    private final DriveSessionRepository driveSessionRepository;
    private final DetectionLogRepository detectionLogRepository;

    /**
     * 포트홀 탐지 정보 처리 및 자동 신고 처리(YOLO 연동)
     * @param dto YOLO에서 전송한 탐지 데이터 (세션ID, 시간, 이미지 경로 등)
     * @throws IllegalArgumentException 유효하지 않은 세션 ID일 경우 예외 발생
     */
    public void processDetection(DetectionRequestDto dto) {
        Long sessionId = dto.sessionId();

        DriveSession session = driveSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세션입니다. sessionId=" + sessionId));

        // 타임스탬프 처리
        int timestamp = dto.videoTimestamp() != null ? dto.videoTimestamp().intValue() : 0;;

        Double coorX = (dto.center() != null) ? dto.center().x() : null;
        Double coorY = (dto.center() != null) ? dto.center().y() : null;

        // 중복 포트홀 검사 (같은 세션, ±2초 이내)
        Optional<Pothole> duplicate = potholeRepository.findDuplicate(sessionId, timestamp - 2, timestamp + 2);

        Pothole pothole;
        if (duplicate.isPresent()) {
            pothole = duplicate.get();
        } else {
            pothole = Pothole.builder()
                    .driveSession(session)
                    .videoTimestamp(timestamp)
                    .severity(PotholeSeverity.valueOf(dto.severity())) // "LOW/MEDIUM/HIGH" 가 enum과 동일하다고 가정
                    .sequenceNumber(dto.totalDetections())            // 이번 세션에서 몇 번째 탐지인지
                    .coordinateX(coorX)                                // YOLO에서 center 좌표를 안 보내므로 일단 null
                    .coordinateY(coorY)
                    .build();

            potholeRepository.save(pothole);
        }

        // 탐지 로그 저장
        DetectionLog log = DetectionLog.builder()
                .pothole(pothole)
                .originalImgPath(dto.images() != null ? dto.images().original() : null)
                .processedImgPath(dto.images() != null ? dto.images().processed() : null)
                .confidenceScore(dto.averageConfidence())
                .build();

        detectionLogRepository.save(log);
    }

    /**
     * 세션별 포트홀 목록 조회
     * @param sessionId 조회할 주행 세션 ID
     * @return 해당 세션의 포트홀 목록 (DTO 리스트)
     */
    @Transactional(readOnly = true)
    public List<PotholeResponseDto> getPotholesBySession(Long sessionId) {
        // 해당 세션의 모든 포트홀 조회
        List<Pothole> potholes = potholeRepository.findAll().stream()
                .filter(p -> p.getDriveSession().getId().equals(sessionId))
                .toList();

        // 각 포트홀마다 로그를 조회해서 DTO로 변환
        return potholes.stream().map(pothole -> {
            List<DetectionLog> logs = detectionLogRepository.findByPotholeId(pothole.getId());

            // 가장 최근 로그 하나 가져오기 (없으면 null)
            DetectionLog latestLog = logs.isEmpty() ? null : logs.get(logs.size() - 1);

            // DTO 변환
            return PotholeResponseDto.from(pothole, latestLog);
        }).toList();
    }
}
