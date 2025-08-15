package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.entity.Flower;
import com.example.gaehwa2.repository.FlowerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FlowerService {

    private final FlowerRepository flowerRepository;

    // 1. 텍스트 데이터 저장
    public Flower saveFlowerText(FlowerRequestDto dto) {
        Flower flower = Flower.builder()
                .flowerFrom(dto.getFlowerFrom())
                .flowerTo(dto.getFlowerTo())
                .relation(dto.getRelation())
                .anniversary(dto.getAnniversary())
                .anvDate(dto.getAnvDate())
                .history(dto.getHistory())
                .build();
        return flowerRepository.save(flower);
    }

    // 2. 이미지 저장
    public Flower saveFlowerImage(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));
        flower.setCardImage(file.getBytes());
        return flowerRepository.save(flower);
    }

    // 3. 음성 저장
    public Flower saveFlowerVoice(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));
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

