package com.raspberrypi.iot.raspberrypi.repository;

import com.raspberrypi.iot.raspberrypi.entity.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightRepository extends JpaRepository<Light, Long> {
    Light findTopByOrderByIdDesc();
}