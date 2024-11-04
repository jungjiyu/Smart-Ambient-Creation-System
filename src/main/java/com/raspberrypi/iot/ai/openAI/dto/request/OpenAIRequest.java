package com.raspberrypi.iot.ai.openAI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class OpenAIRequest {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Basic {
        private String model;
        private String prompt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextCompose {
        private String model;
        private List<String> sentences;
    }



}