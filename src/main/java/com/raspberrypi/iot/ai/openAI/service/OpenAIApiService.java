package com.raspberrypi.iot.ai.openAI.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raspberrypi.iot.ai.openAI.dto.request.OpenAIRequest;
import com.raspberrypi.iot.raspberrypi.dto.request.SensorDataDTO;
import com.raspberrypi.iot.raspberrypi.enums.LedColor;
import com.raspberrypi.iot.raspberrypi.enums.SensorDataLevel;
import com.raspberrypi.iot.raspberrypi.service.SensorService;
import com.raspberrypi.iot.raspberrypi.util.SensorDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpenAIApiService {
    @Value("${openai.api.key}")
    private String apiKey;
    private final SensorDataUtil sensorDataUtil;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();


    public String complete(OpenAIRequest.Basic openAIRequest) throws IOException {
        String jsonBody = String.format(
                "{ \"model\": \"%s\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"You are a helpful assistant.\"}," +
                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
                openAIRequest.getModel(),
                openAIRequest.getPrompt()
        );

        log.info("Request Body: {}", jsonBody);

        return executeRequest(jsonBody);
    }


    public String createPoem(SensorDataDTO latestSensorData) throws IOException  {
        String model = "gpt-4o-mini" ; // 다른 모델도 선택 가능
        List<SensorDataLevel> sensorLevels = sensorDataUtil.classifyAll(latestSensorData);

        // Prompt 작성
        String prompt = sensorDataUtil.convertAllDescriptions(sensorLevels);

        String jsonBody = String.format(
                "{ \"model\": \"%s\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"You are a poet who writes poetry in korean that matches the given context. Provide only the poem without any additional explanation or commentary.\"}," +
                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
                model,
                prompt
        );

        log.info("Request Body: {}", jsonBody);


        String responseBody = executeRequest(jsonBody);
        return extractResult(responseBody);


    }





    /**
     * 센서 데이터 수준에 따른 YouTube URL 추천
     *
     * @param sensorLevels 센서 데이터 수준 (온도, 소음, 조도)
     * @return 추천된 YouTube URL
     * @throws IOException API 호출 에러
     */
    public String recommendYoutubeUrl(List<SensorDataLevel> sensorLevels) throws IOException  {
        String model = "gpt-4o-mini" ; // 다른 모델도 선택 가능

        // Prompt 작성
        String prompt = sensorDataUtil.convertAllDescriptions(sensorLevels);

        String jsonBody = String.format(
                "{ \"model\": \"%s\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"Provide a YouTube video URL that matches the given context. Provide only the URL and no additional explanation.\"}," +
                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
                model,
                prompt
        );

        log.info("Request Body: {}", jsonBody);


        String responseBody = executeRequest(jsonBody);
        return extractResult(responseBody);


    }


    public String recommendLedColor(List<SensorDataLevel> sensorLevels) throws IOException {
        String model = "gpt-4o-mini";

        String prompt = sensorDataUtil.convertAllDescriptions(sensorLevels);

        // OpenAI API 요청 본문 생성
        String jsonBody = String.format(
                "{ \"model\": \"%s\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"You are an assistant that recommends an appropriate LED color based on the given context. Return one of the following values: 'red' or 'blue'. Provide only the value and no additional explanation.\"}," +
                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
                model,
                prompt
        );

        log.info("Request Body: {}", jsonBody);

        String responseBody = executeRequest(jsonBody);
        return extractResult(responseBody);

    }






    private String executeRequest(String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.get("application/json"), jsonBody
        );

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        log.info("apiKey: {}", apiKey);

        log.info("Request: {}", request);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }


    // JSON 응답에서 결론 부분만 추출하는 메서드 (단순 예시)
    private String extractResult( String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // 응답에서 'choices' 배열을 가져오고, 첫 번째 choice에서 메시지 내용 추출
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        return content;
    }







}
