package com.raspberrypi.iot.sensor.service;

import com.raspberrypi.iot.sensor.dto.SensorDataDTO;
import com.raspberrypi.iot.sensor.entity.Light;
import com.raspberrypi.iot.sensor.entity.Sound;
import com.raspberrypi.iot.sensor.entity.TemperatureHumidity;
import com.raspberrypi.iot.sensor.repository.LightRepository;
import com.raspberrypi.iot.sensor.repository.SoundRepository;
import com.raspberrypi.iot.sensor.repository.TemperatureHumidityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SensorService {

    private final TemperatureHumidityRepository temperatureHumidityRepository;
    private final LightRepository lightRepository;
    private final SoundRepository soundRepository;

    public void saveTemperatureHumidity(float temperature, float humidity) {
        TemperatureHumidity tempHumid = new TemperatureHumidity();
        tempHumid.setTemperature(temperature);
        tempHumid.setHumidity(humidity);
        temperatureHumidityRepository.save(tempHumid);
    }

    public void saveLight(int lightSensorValue, float lightResistance) {
        Light lightSensor = new Light();
        lightSensor.setLightSensorValue(lightSensorValue);
        lightSensor.setLightResistance(lightResistance);
        lightRepository.save(lightSensor);
    }

    public void saveSound(int soundLevel) {
        Sound soundSensor = new Sound();
        soundSensor.setSoundLevel(soundLevel);
        soundRepository.save(soundSensor);
    }

    public SensorDataDTO getLatestSensorData() {
        // 각각의 센서 데이터 중 가장 최근에 저장된 데이터를 가져옴
        TemperatureHumidity latestTempHumid = temperatureHumidityRepository.findTopByOrderByIdDesc();
        Light latestLightSensor = lightRepository.findTopByOrderByIdDesc();
        Sound latestSoundSensor = soundRepository.findTopByOrderByIdDesc();

        // SensorDataDTO로 종합하여 반환
        SensorDataDTO sensorDataDTO =  SensorDataDTO.builder()
                .temperature(latestTempHumid.getTemperature())
                .humidity(latestTempHumid.getHumidity())
                .lightSensorValue(latestLightSensor.getLightSensorValue())
                .lightResistance(latestLightSensor.getLightResistance())
                .soundLevel(latestSoundSensor.getSoundLevel())
                .build();

        log.info("latest sensorDataDto: {}",sensorDataDTO );
        return sensorDataDTO;
    }


}
