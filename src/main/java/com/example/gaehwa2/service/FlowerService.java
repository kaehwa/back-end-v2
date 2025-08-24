package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.response.FastApiRecommendResponseDto;
import com.example.gaehwa2.dto.response.FlowerFromToResponseDto;
import com.example.gaehwa2.dto.response.FlowerMediaResponseDto;
import com.example.gaehwa2.dto.response.MediaUploadResponseDto;
import com.example.gaehwa2.entity.Bouquet;
import com.example.gaehwa2.entity.Flower;
import com.example.gaehwa2.entity.Medialetter;
import com.example.gaehwa2.repository.FlowerRepository;
import com.example.gaehwa2.utils.ColorUtils;
import com.pgvector.PGvector;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;




@Service
@RequiredArgsConstructor
public class FlowerService {

    private final FlowerRepository flowerRepository;
    private final FastApiService fastApiService;
    private final AzureBlobService azureBlobService;

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
    public MediaUploadResponseDto saveFlowerImage(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));

        flower.setCardImage(file.getBytes());
        flowerRepository.save(flower);

        return MediaUploadResponseDto.fromEntity(flower);
    }

    // 3. 음성 저장
    public MediaUploadResponseDto saveFlowerVoice(Long id, MultipartFile file) throws IOException {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 꽃 데이터를 찾을 수 없습니다."));

        flower.setCardVoice(file.getBytes());
        flowerRepository.save(flower);

        return MediaUploadResponseDto.fromEntity(flower);
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

    @Transactional(readOnly = true)
    public FlowerMediaResponseDto getFlowerMedia(Long id) {
        // 1. Flower 조회
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flower ID not found"));

        // 2. Medialetter 조회
        Medialetter medialetter = flower.getMedialetter();

        // 3. Bouquet 조회
        Bouquet bouquet = flower.getBouquet();

        // 4. voiceletter Base64 인코딩
        String voiceletterBase64 = null;
        if (medialetter != null && medialetter.getVoiceletter() != null) {
            voiceletterBase64 = Base64.getEncoder().encodeToString(medialetter.getVoiceletter());
        }

        // DB에 저장된 원본 URL
        String originalUrl = medialetter != null ? medialetter.getVideoletterUrl() : null;
        String sasUrl = null;

        if (originalUrl != null) {
            // blobName 추출
            String blobName = URLDecoder.decode(
                    originalUrl.substring(originalUrl.lastIndexOf("/") + 1),
                    StandardCharsets.UTF_8
            );
            sasUrl = azureBlobService.generateSasUrl("gae-container", "videos/" + blobName);
        }

        // 5. Response DTO 구성
        return FlowerMediaResponseDto.builder()
                .flowerId(flower.getId())
                .recommendMessage(flower.getRecommendMessage())
                .bouquetVideoUrl(bouquet != null ? bouquet.getBouquetVideoUrl() : null)
                .bouquetRgb(bouquet != null ? bouquet.getBouquetRgb() : null)
                .voiceletterBase64(voiceletterBase64)
                .videoletterUrl(sasUrl)
                .build();
    }

    @Transactional(readOnly = true)
    public FlowerFromToResponseDto getFlowerFromTo(Long id) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flower ID not found"));

        return FlowerFromToResponseDto.builder()
                .id(flower.getId())
                .flowerFrom(flower.getFlowerFrom())
                .flowerTo(flower.getFlowerTo())
                .build();
    }
}



