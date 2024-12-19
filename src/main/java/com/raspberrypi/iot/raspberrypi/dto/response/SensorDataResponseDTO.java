package com.raspberrypi.iot.raspberrypi.dto.response;

import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.enums.LedColor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 라즈베리파이로부터 정상적으로 데이터 수신 시 반환하는 dto
 * 다음을 전송 : 수집된 데이터, 라즈베리파이가 활용할 youtube url 과 led 색상
 */
@ToString
@Builder
@Getter
@Setter
public class SensorDataResponseDTO {

    // 수집된 데이터
    private SensorDataDTO inputData;
    // 라즈베리 파이가 재생할 youtube_url
    private String youtubeUrl;
    // 라즈베리파이가 출력할 LED 색상
    private String ledColor;


}
