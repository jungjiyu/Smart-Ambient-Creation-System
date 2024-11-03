package com.raspberrypi.iot.sensor.repository;

import com.raspberrypi.iot.sensor.entity.TemperatureHumidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureHumidityRepository extends JpaRepository<TemperatureHumidity, Long> {
    TemperatureHumidity findTopByOrderByIdDesc();

}