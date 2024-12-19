package com.raspberrypi.iot.raspberrypi.controller;

import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.dto.response.SensorDataResponseDTO;
import com.raspberrypi.iot.raspberrypi.enums.LedColor;
import com.raspberrypi.iot.raspberrypi.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensor")
//@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 URL
public class RaspberrypiController {
    private final SensorService sensorService;
    private LedColor currentLedColor = LedColor.OFF;  // 기본 LED 색상

    /**
     * 라즈베리파이로부터 센서 데이터 입력받고
     * 특정한 기준에 매칭시켜 youtube_url 과 led 색상을 raspberrypi 쪽으로 반ㅇ환
     * ( 시 같은거는 제공할 필요 없음 - 라즈베리 파이만 이 api 쓰는건데, 사용자는 라즈베리파이를 직접 볼 일은 없으니까)
     * @param sensorDataDTO
     * @return
     */
    @PostMapping("/data")
    public ResponseEntity<SensorDataResponseDTO> receiveSensorData(@RequestBody SensorDataDTO sensorDataDTO) throws IOException {
        log.info("got data: {}", sensorDataDTO);
        SensorDataResponseDTO responseDTO = sensorService.receiveSensorData(sensorDataDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/latest")
    public  ResponseEntity<SensorDataDTO> getLatestSensorData() {
        return ResponseEntity.ok(sensorService.getLatestSensorData());
    }



}
