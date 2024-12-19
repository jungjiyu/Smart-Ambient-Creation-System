package com.raspberrypi.iot.raspberrypi.repository;

import com.raspberrypi.iot.raspberrypi.entity.TemperatureHumidity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemperatureHumidityRepository extends JpaRepository<TemperatureHumidity, Long> {
    Optional<TemperatureHumidity> findTopByOrderByIdDesc();

}