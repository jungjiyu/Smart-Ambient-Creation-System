package com.raspberrypi.iot.raspberrypi.service;

import com.raspberrypi.iot.ai.openAI.service.OpenAIApiService;
import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.dto.response.SensorDataResponseDTO;
import com.raspberrypi.iot.raspberrypi.entity.Light;
import com.raspberrypi.iot.raspberrypi.entity.Sound;
import com.raspberrypi.iot.raspberrypi.entity.TemperatureHumidity;
import com.raspberrypi.iot.raspberrypi.enums.SensorDataLevel;
import com.raspberrypi.iot.raspberrypi.repository.LightRepository;
import com.raspberrypi.iot.raspberrypi.repository.SoundRepository;
import com.raspberrypi.iot.raspberrypi.repository.TemperatureHumidityRepository;
import com.raspberrypi.iot.raspberrypi.util.SensorDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SensorService {

    private final TemperatureHumidityRepository temperatureHumidityRepository;
    private final LightRepository lightRepository;
    private final SoundRepository soundRepository;
    private final SensorDataUtil sensorDataUtil;
    private final OpenAIApiService openAIApiService;




    public SensorDataResponseDTO receiveSensorData(SensorDataDTO sensorDataDTO) throws IOException  {
        // 센서 데이터 저장
        saveSensorDatas(sensorDataDTO);
        // 센서 데이터 특정 기준치로 변환
        List<SensorDataLevel> levels = sensorDataUtil.classifyAll(sensorDataDTO);
        // 변환한 기준치를 바탕으로 yt_url 과 led 색상 판별
        String youtubeUrl = openAIApiService.recommendYoutubeUrl(levels);
        String ledColor = openAIApiService.recommendLedColor(levels);

        SensorDataResponseDTO responseDTO = SensorDataResponseDTO.builder()
                        .inputData(sensorDataDTO)
                        .youtubeUrl(youtubeUrl)
                        .ledColor(ledColor).build();

        return responseDTO;
    }

    public void saveSensorDatas(SensorDataDTO sensorDataDTO){
        saveTemperatureHumidity(sensorDataDTO.getTemperature(), sensorDataDTO.getHumidity());
        saveLight(sensorDataDTO.getLightSensorValue(), sensorDataDTO.getLightResistance());
        saveSound(sensorDataDTO.getSoundLevel());
    }

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

        log.info("latest sensorDataDto: {}", sensorDataDTO);
        return sensorDataDTO;
    }


}