package com.raspberrypi.iot.raspberrypi.util;


import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.enums.SensorDataLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class SensorDataUtil {

    /**
     * 온습도 데이터를 판별하여 결과 반환
     * @param temperature 온도 값
     * @return SensorDataLevel (LOW, MEDIUM, HIGH)
     */
    public SensorDataLevel classifyTemperature(double temperature) {
        if (temperature < 10) {
            return SensorDataLevel.LOW; // 춥다
        } else if (temperature <= 20) {
            return SensorDataLevel.MEDIUM; // 적당하다
        } else {
            return SensorDataLevel.HIGH; // 덥다
        }
    }

    /**
     * 소리 데이터를 판별하여 결과 반환
     * @param soundLevel 소음 값
     * @return SensorDataLevel (LOW, MEDIUM, HIGH)
     */
    public SensorDataLevel classifySoundLevel(int soundLevel) {
        if (soundLevel < 300) {
            return SensorDataLevel.LOW; // 조용하다
        } else if (soundLevel <= 700) {
            return SensorDataLevel.MEDIUM; // 적당하다
        } else {
            return SensorDataLevel.HIGH; // 시끄럽다
        }
    }

    /**
     * 조도 데이터를 판별하여 결과 반환
     * @param lightResistance 조도 값
     * @return SensorDataLevel (LOW, MEDIUM, HIGH)
     */
    public SensorDataLevel classifyLightLevel(double lightResistance) {
        if (lightResistance < 300) {
            return SensorDataLevel.LOW; // 어둡다
        } else if (lightResistance <= 700) {
            return SensorDataLevel.MEDIUM; // 적당하다
        } else {
            return SensorDataLevel.HIGH; // 밝다
        }
    }

    /**
     * 전체 데이터를 기반으로 모든 결과를 반환
     * @param sensorDataDTO 센서 데이터 DTO
     * @return List<SensorDataLevel> 판별된 센서 레벨 리스트
     */
    public List<SensorDataLevel> classifyAll(SensorDataDTO sensorDataDTO) {
        List<SensorDataLevel> levels = new ArrayList<>();

        // 온도 판별
        SensorDataLevel tempLevel = classifyTemperature(sensorDataDTO.getTemperature());
        levels.add(tempLevel);

        // 소음 판별
        SensorDataLevel soundLevel = classifySoundLevel(sensorDataDTO.getSoundLevel());
        levels.add(soundLevel);

        // 조도 판별
        SensorDataLevel lightLevel = classifyLightLevel(sensorDataDTO.getLightResistance());
        levels.add(lightLevel);

        return levels;
    }



    /**
     * 온도 설명 변환
     */
    public String convertTemperatureDescription(SensorDataLevel level) {
        switch (level) {
            case LOW:
                return "cold";
            case MEDIUM:
                return "comfortable";
            case HIGH:
                return "hot";
            default:
                return "unknown";
        }
    }

    /**
     * 소리 설명 변환
     */
    public String convertSoundDescription(SensorDataLevel level) {
        switch (level) {
            case LOW:
                return "quiet";
            case MEDIUM:
                return "moderate";
            case HIGH:
                return "noisy";
            default:
                return "unknown";
        }
    }

    /**
     * 조도 설명 변환
     */
    public String convertLightDescription(SensorDataLevel level) {
        switch (level) {
            case LOW:
                return "dark";
            case MEDIUM:
                return "normal brightness";
            case HIGH:
                return "bright";
            default:
                return "unknown";
        }
    }

    /**
     * 전체 데이터를 기반으로 설명을 반환
     */
    public String convertAllDescriptions(List<SensorDataLevel> sensorDataLevels) {
        String temperatureDescription = convertTemperatureDescription(sensorDataLevels.get(0));
        String soundDescription = convertSoundDescription(sensorDataLevels.get(1));
        String lightDescription = convertLightDescription(sensorDataLevels.get(2));

        return String.format("Temperature: %s, Sound: %s, Light: %s",
                temperatureDescription, soundDescription, lightDescription);
    }
}

