package com.raspberrypi.iot.raspberrypi.repository;

import com.raspberrypi.iot.raspberrypi.entity.Light;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightRepository extends JpaRepository<Light, Long> {
    Optional<Light> findTopByOrderByIdDesc();
}