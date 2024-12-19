package com.raspberrypi.iot.ai.openAI.controller;

import com.raspberrypi.iot.ai.openAI.dto.request.OpenAIRequest;
import com.raspberrypi.iot.ai.openAI.service.OpenAIApiService;
import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.enums.SensorDataLevel;
import com.raspberrypi.iot.raspberrypi.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openai")
public class OpenAIApiController {

    private final OpenAIApiService openAIService;
    private final SensorService sensorService;


    @PostMapping("/complete")
    public String complete(@RequestBody OpenAIRequest.Basic openAIRequest) throws IOException {
        return openAIService.complete(openAIRequest);
    }

    @PostMapping("/poem")
    public String createPoem() throws IOException {
        SensorDataDTO latestSensorData = sensorService.getLatestSensorData();
        return openAIService.createPoem(latestSensorData);
    }

    @PostMapping("/youtube")
    public String recommendYoutubeUrl() throws IOException {
        SensorDataDTO latestSensorData = sensorService.getLatestSensorData();
        return openAIService.recommendYoutubeUrl(latestSensorData);
    }

    @PostMapping("/led")
    public String recommendLedColor() throws IOException {
        SensorDataDTO latestSensorData = sensorService.getLatestSensorData();
        return openAIService.recommendLedColor(latestSensorData);
    }


}
