package com.potping.domain.session.service;

import com.potping.domain.region.entity.Region;
import com.potping.domain.region.repository.RegionRepository;
import com.potping.domain.session.dto.request.SessionRequestDto;
import com.potping.domain.session.entity.DriveSession;
import com.potping.domain.session.repository.DriveSessionRepository;
import com.potping.domain.user.entity.User;
import com.potping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriveSessionService {
    private final DriveSessionRepository driveSessionRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    /**
     * 주행 세션 시작
     * @return 생성된 세션 ID
     */
    @Transactional
    public Long startSession(SessionRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Region region = regionRepository.findById(dto.regionId())
                .orElseThrow(() -> new IllegalArgumentException("지역을 찾을 수 없습니다."));

        DriveSession session = DriveSession.builder()
                .user(user)
                .region(region)
                .build();

        driveSessionRepository.save(session);
        return session.getId();
    }

    /**
     * 주행 세션 종료
     */
    @Transactional
    public void endSession(Long sessionId) {
        DriveSession session = driveSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        session.endSession(); // 종료 시간 기록
    }
}
