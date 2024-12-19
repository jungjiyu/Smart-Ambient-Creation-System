package com.raspberrypi.iot.ai.openAI.controller;

import com.raspberrypi.iot.ai.openAI.dto.request.OpenAIRequest;
import com.raspberrypi.iot.ai.openAI.service.OpenAIApiService;
import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.enums.SensorDataLevel;
import com.raspberrypi.iot.raspberrypi.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openai")
public class OpenAIApiController {

    private final OpenAIApiService openAIService;
    private final SensorService sensorService;


    // 프롬프트 직접
    @PostMapping("/complete")
    public String complete(@RequestBody OpenAIRequest.Basic openAIRequest) throws IOException {
        return openAIService.complete(openAIRequest);
    }

    // 시 작성
    @PostMapping("/poem")
    public String createPoem() throws IOException {
        SensorDataDTO latestSensorData = sensorService.getLatestSensorData();
        return openAIService.createPoem(latestSensorData);
    }




    //테스트용. 직접 호출할 일은 x
    @PostMapping("/youtube")
    public String recommendYoutubeUrl(@RequestBody List<SensorDataLevel> sensorLevels) throws IOException {
        return openAIService.recommendYoutubeUrl(sensorLevels);
    }

    //테스트용. 직접 호출할 일은 x
    @PostMapping("/led")
    public String recommendLedColor(@RequestBody List<SensorDataLevel> sensorLevels) throws IOException {
        return openAIService.recommendLedColor(sensorLevels);
    }






//
//    // 여러 문장을 하나로 합쳐 글을 작성
//    @PostMapping("/compose")
//    public String composeText(@RequestBody  OpenAIRequest.TextCompose request) throws IOException {
//        return openAIService.composeText(request);
//    }
//

}
