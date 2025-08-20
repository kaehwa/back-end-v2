package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.request.FastApiRecommendRequestDto;
import com.example.gaehwa2.dto.response.FastApiRecommendResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class FastApiService {

    private final WebClient webClient;

    public FastApiService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://127.0.0.1:8000/api/v1").build();
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
}



