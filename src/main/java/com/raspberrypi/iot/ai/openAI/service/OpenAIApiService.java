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

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    private final SensorDataUtil sensorDataUtil;
    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search";
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




//
//    /**
//     * 센서 데이터 수준에 따른 YouTube URL 추천
//     *
//     * @param sensorLevels 센서 데이터 수준 (온도, 소음, 조도)
//     * @return 추천된 YouTube URL
//     * @throws IOException API 호출 에러
//     */
//    public String recommendYoutubeUrl(List<SensorDataLevel> sensorLevels) throws IOException  {
//        String model = "gpt-4o-mini" ; // 다른 모델도 선택 가능
//
//        // Prompt 작성
//        String prompt = sensorDataUtil.convertAllDescriptions(sensorLevels);
//
//        String jsonBody = String.format(
//                "{ \"model\": \"%s\", \"messages\": [" +
//                        "{ \"role\": \"system\", \"content\": \"Provide a YouTube video URL that matches the given context. Provide only the URL and no additional explanation.\"}," +
//                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
//                model,
//                prompt
//        );
//
//        log.info("Request Body: {}", jsonBody);
//
//
//        String responseBody = executeRequest(jsonBody);
//        return extractResult(responseBody);
//
//
//    }

    public String recommendYoutubeUrl(SensorDataDTO latestSensorData) throws IOException {
        List<SensorDataLevel> sensorLevels = sensorDataUtil.classifyAll(latestSensorData);
        // Step 1: AI로부터 노래 제목 추천받기
        String songTitle = recommendYoutubeTitle(sensorLevels);
        log.info("Recommended Song Title: {}", songTitle);

        // Step 2: 추천받은 노래 제목으로 유튜브 URL 검색
        String youtubeUrl = searchYoutubeUrl(songTitle);
        log.info("YouTube URL: {}", youtubeUrl);

        return youtubeUrl;
    }

    private String searchYoutubeUrl(String songTitle) throws IOException {
        String query = String.format("%s", songTitle);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(YOUTUBE_API_URL).newBuilder();
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("q", query);
        urlBuilder.addQueryParameter("type", "video");
        urlBuilder.addQueryParameter("maxResults", "1");
        urlBuilder.addQueryParameter("key", youtubeApiKey);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .get()
                .build();

        log.info("YouTube API Request: {}", request);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // YouTube API 응답에서 URL 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body().string());
            JsonNode items = rootNode.path("items").get(0);
            String videoId = items.path("id").path("videoId").asText();

            return "https://www.youtube.com/watch?v=" + videoId;
        }
    }



    public String recommendYoutubeTitle(List<SensorDataLevel> sensorLevels) throws IOException {
        String model = "gpt-4o-mini";

        // Prompt 작성
        String prompt = sensorDataUtil.convertAllDescriptions(sensorLevels);

        String jsonBody = String.format(
                "{ \"model\": \"%s\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"Provide a song title that matches the given context. Provide only the title without any additional explanation.\"}," +
                        "{ \"role\": \"user\", \"content\": \"%s\"}]}",
                model,
                prompt
        );

        log.info("Request Body: {}", jsonBody);

        String responseBody = executeRequest(jsonBody);
        return extractResult(responseBody);
    }


    public String recommendLedColor(SensorDataDTO latestSensorData) throws IOException {
        String model = "gpt-4o-mini";
        List<SensorDataLevel> sensorLevels = sensorDataUtil.classifyAll(latestSensorData);
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
