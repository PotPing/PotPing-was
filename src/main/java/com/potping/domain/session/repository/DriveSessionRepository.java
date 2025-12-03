package com.potping.domain.session.repository;

import com.potping.domain.session.entity.DriveSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveSessionRepository extends JpaRepository<DriveSession, Long> {
}
