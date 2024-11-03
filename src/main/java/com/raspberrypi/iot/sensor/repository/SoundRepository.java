package com.raspberrypi.iot.sensor.repository;

import com.raspberrypi.iot.sensor.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoundRepository extends JpaRepository<Sound, Long> {
    Sound findTopByOrderByIdDesc();
}