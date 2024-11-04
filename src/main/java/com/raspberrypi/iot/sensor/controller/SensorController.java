package com.raspberrypi.iot.sensor.controller;

import com.raspberrypi.iot.sensor.dto.SensorDataDTO;
import com.raspberrypi.iot.sensor.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensor")
public class SensorController {
    private final SensorService sensorService;
    private String currentLedColor = "off";  // 기본 LED 색상

    @PostMapping("/data")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataDTO sensorDataDTO) {
        log.info("got data: {}",sensorDataDTO);
        sensorService.saveTemperatureHumidity(sensorDataDTO.getTemperature(), sensorDataDTO.getHumidity());
        sensorService.saveLight(sensorDataDTO.getLightSensorValue(), sensorDataDTO.getLightResistance());
        sensorService.saveSound(sensorDataDTO.getSoundLevel());

        SensorDataDTO latestData = sensorService.getLatestSensorData();

        //TODO: AI API 호출하여 추천 LED 색상 결정

        return ResponseEntity.ok(latestData.toString());
    }

    @GetMapping("/latest")
    public  ResponseEntity<SensorDataDTO> getLatestSensorData() {
        return ResponseEntity.ok(sensorService.getLatestSensorData());
    }

    @GetMapping("/led/control")
    public String getCurrentLedColor() {
        // 현재 LED 색상을 반환
        return currentLedColor;
    }


}
