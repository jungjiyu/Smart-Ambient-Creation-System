package com.raspberrypi.iot.raspberrypi.repository;

import com.raspberrypi.iot.raspberrypi.entity.TemperatureHumidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureHumidityRepository extends JpaRepository<TemperatureHumidity, Long> {
    TemperatureHumidity findTopByOrderByIdDesc();

}