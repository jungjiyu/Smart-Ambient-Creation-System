package com.raspberrypi.iot.ai.openAI.controller;

import com.raspberrypi.iot.ai.openAI.dto.request.OpenAIRequest;
import com.raspberrypi.iot.ai.openAI.service.OpenAIApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openai")
public class OpenAIApiController {

    private final OpenAIApiService openAIService;


    // 프롬프트 직접
    @PostMapping("/complete")
    public String complete(@RequestBody OpenAIRequest.Basic openAIRequest) throws IOException {
        return openAIService.complete(openAIRequest);
    }

    // 여러 문장을 하나로 합쳐 글을 작성
    @PostMapping("/compose")
    public String composeText(@RequestBody  OpenAIRequest.TextCompose request) throws IOException {
        return openAIService.composeText(request);
    }


}
