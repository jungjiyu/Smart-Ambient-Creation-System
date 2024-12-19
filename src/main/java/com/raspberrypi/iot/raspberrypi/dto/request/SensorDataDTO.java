package com.raspberrypi.iot.raspberrypi.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 라즈베리파이로부터 센서 데이터 받아오는 DTO
 */
@ToString
@Builder
@Getter
@Setter
public class SensorDataDTO {
    private float temperature;
    private float humidity;
    private int lightSensorValue;
    private float lightResistance;
    private int soundLevel;
}
