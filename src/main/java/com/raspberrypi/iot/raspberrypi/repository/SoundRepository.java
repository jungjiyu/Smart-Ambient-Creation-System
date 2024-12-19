package com.raspberrypi.iot.raspberrypi.repository;

import com.raspberrypi.iot.raspberrypi.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoundRepository extends JpaRepository<Sound, Long> {
    Optional<Sound> findTopByOrderByIdDesc();
}