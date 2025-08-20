package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.response.FastApiRecommendResponseDto;
import com.example.gaehwa2.entity.Flower;
import com.example.gaehwa2.repository.FlowerRepository;
import com.example.gaehwa2.utils.ColorUtils;
import com.pgvector.PGvector;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;




@Service
@RequiredArgsConstructor
public class FlowerService {

    private final FlowerRepository flowerRepository;
    private final FastApiService fastApiService;
    // 1. 텍스트 데이터 저장
    public Flower saveFlowerText(FlowerRequestDto dto) {

        // 1. FastAPI 호출 → 응답 DTO 받기
        FastApiRecommendResponseDto response = fastApiService.callFastApi(dto);

        // 2. 응답에서 rgb_selected(2D 배열) → 12차원 벡터 변환
        float[] flattened = ColorUtils.flattenRgb(response.getRgb_selected());
        PGvector vector = new PGvector(flattened);

        // 3. 요청 DTO + 응답 DTO → 엔티티 저장
        Flower flower = Flower.builder()
                .flowerFrom(dto.getFlowerFrom())
                .flowerTo(dto.getFlowerTo())
                .relation(dto.getRelation())
                .anniversary(dto.getAnniversary())
                .anvDate(dto.getAnvDate())
                .history(dto.getHistory())
                .recommendRgbVector(vector)              // FastAPI 응답
                .recommendMessage(response.getMessage()) // FastAPI 응답
                .build();

        return flowerRepository.save(flower);

    }
    // 2. 이미지 저장
    public Flower saveFlowerImage(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));

        // MultipartFile에서 바로 byte[] 저장
        flower.setCardImage(file.getBytes());

        return flowerRepository.save(flower);
    }

    // 3. 음성 저장
    public Flower saveFlowerVoice(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));

        // MultipartFile에서 바로 byte[] 저장
        flower.setCardVoice(file.getBytes());

        return flowerRepository.save(flower);
    }


    @Transactional(readOnly = true)
    public String getRecommendMessage(Long id) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flower not found"));
        return flower.getRecommendMessage();
    }

    @Transactional
    public String updateRecommendMessage(Long id, String recommendMessage) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flower not found"));
        flower.setRecommendMessage(recommendMessage);
        return flower.getRecommendMessage();
    }


}



