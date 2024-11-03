package com.raspberrypi.iot.sensor.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
