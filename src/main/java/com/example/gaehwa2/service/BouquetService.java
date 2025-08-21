// BouquetService.java
package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.response.BouquetResponseDto;
import com.example.gaehwa2.entity.Bouquet;
import com.example.gaehwa2.repository.BouquetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BouquetService {

    private final BouquetRepository bouquetRepository;

    public List<BouquetResponseDto> getSimilarBouquets(Long flowerId) {
        List<Bouquet> bouquets = bouquetRepository.findTop4ByCosineSimilarity(flowerId);

        return bouquets.stream().map(b -> {
            String imageBase64 = null;

            if (b.getBouquetImage() != null) {
                imageBase64 = Base64.getEncoder().encodeToString(b.getBouquetImage());
                // Base64 문자열 로그 찍기
//                System.out.println("Encoded Base64: " + imageBase64.substring(0, 50) + "...");
//                // 너무 길어서 앞부분만 찍음
            }

            return BouquetResponseDto.builder()
                    .id(b.getId())
                    .name(b.getBouquetName())
                    .imageBase64(imageBase64)
                    .rgb(b.getBouquetRgb() != null ? convertVectorToIntList(b.getBouquetRgb()) : null)
                    .anniversaryName(b.getAnniversaryName())
                    .emotionName(b.getEmotion())
                    .build();
        }).collect(Collectors.toList());
    }

    private List<List<Integer>> convertVectorToIntList(com.pgvector.PGvector vector) {
        List<List<Integer>> rgbList = new java.util.ArrayList<>();
        float[] values = vector.toArray(); // PGvector를 float 배열로 변환
        for (int i = 0; i < values.length; i += 3) {
            rgbList.add(List.of(
                    Math.round(values[i]),
                    Math.round(values[i + 1]),
                    Math.round(values[i + 2])
            ));
        }
        return rgbList;
    }
}
