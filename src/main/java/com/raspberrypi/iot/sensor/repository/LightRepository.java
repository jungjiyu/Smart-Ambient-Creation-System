package com.raspberrypi.iot.sensor.repository;

import com.raspberrypi.iot.sensor.entity.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightRepository extends JpaRepository<Light, Long> {
    Light findTopByOrderByIdDesc();
}