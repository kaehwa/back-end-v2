package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.request.FastApiRecommendRequestDto;
import com.example.gaehwa2.dto.response.FastApiRecommendResponseDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;


@Service
public class FastApiService {

    private final WebClient webClient;
    private final AzureBlobService azureBlobService;

    public FastApiService(WebClient.Builder builder, AzureBlobService azureBlobService) {
        this.webClient = builder.baseUrl("http://127.0.0.1:8000/api/v1").build();
        this.azureBlobService = azureBlobService;
    }

    public FastApiRecommendResponseDto callFastApi(FlowerRequestDto dto) {
        FastApiRecommendRequestDto request = new FastApiRecommendRequestDto();
        request.setWhen_text(dto.getAnniversary());
        request.setActor(dto.getFlowerFrom());
        request.setRecipient(dto.getFlowerTo());
        request.setRelationship(dto.getRelation());
        request.setHistory(dto.getHistory());
        request.setRecipient_gender("");
        request.setRgb_target(5);

        return webClient.post()
                .uri("/recommend")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FastApiRecommendResponseDto.class)
                .block();
    }

    private final RestTemplate restTemplate = new RestTemplate();
   // private final AzureBlobService azureBlobService;

    public byte[] callTtsClone(byte[] voiceFile, String text) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("sample", new ByteArrayResource(voiceFile) {
            @Override
            public String getFilename() {
                return "voice.wav";
            }
        });
        body.add("text", text);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:9000/tts/clone",
                new HttpEntity<>(body, createMultipartHeaders()),
                Map.class
        );

        // URL에서 mp3 다운로드
        // 항상 고정 절대 URL 사용
        String mp3Url = "http://localhost:9000/outputs/voice_final.mp3";

// 다운로드
        return restTemplate.getForObject(mp3Url, byte[].class);
//        String mp3Url = (String) response.getBody().get("http://localhost:9000/static/voice_final.mp3");
//        return restTemplate.getForObject(mp3Url, byte[].class);
    }

    public String callOneClick(byte[] imageFile, String userText) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("user_text", userText);
        body.add("image", new ByteArrayResource(imageFile) {
            @Override
            public String getFilename() {
                return "Image_Editor.png";
            }
        });
        body.add("outfile", "veo_output.mp4");
        body.add("aspect_ratio", "16:9");
        body.add("poll_seconds", 8);
        body.add("quality", "fast");
        body.add("reuse_cached", true);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://0.0.0.0:9001/oneclick",
                new HttpEntity<>(body, createMultipartHeaders()),
                Map.class
        );

        String savedPath = (String) response.getBody().get("saved_path");

        // Azure Blob 업로드
        return azureBlobService.uploadFile(savedPath, "videos/flower_" + System.currentTimeMillis() + ".mp4");
    }

    private HttpHeaders createMultipartHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }
}



