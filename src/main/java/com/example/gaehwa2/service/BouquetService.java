// BouquetService.java
package com.example.gaehwa2.service;

import com.example.gaehwa2.dto.response.BouquetResponseDto;
import com.example.gaehwa2.entity.Bouquet;
import com.example.gaehwa2.repository.BouquetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BouquetService {

    private final BouquetRepository bouquetRepository;

    public List<BouquetResponseDto> getSimilarBouquets(Long flowerId) {
        List<Bouquet> bouquets = bouquetRepository.findTop4ByCosineSimilarity(flowerId);

        return bouquets.stream().map(b -> BouquetResponseDto.builder()
                .id(b.getId())
                .name(b.getBouquetName())
                .imageBase64(b.getBouquetImage() != null ? Base64.getEncoder().encodeToString(b.getBouquetImage()) : null)
                .rgb(b.getBouquetRgb() != null ? convertVectorToIntList(b.getBouquetRgb()) : null)
                .anniversaryName(b.getAnniversaryName())
                .emotionName(b.getEmotion())
                .build()
        ).collect(Collectors.toList());
    }

    private List<List<Integer>> convertVectorToIntList(com.pgvector.PGvector vector) {
        List<List<Integer>> rgbList = new ArrayList<>();
        if (vector == null) return rgbList;

        // PGvector 내부 문자열 가져오기
        String vectorStr = vector.toString(); // "{231.0,76.0,60.0,...}"
        vectorStr = vectorStr.replace("{", "").replace("}", "");
        String[] parts = vectorStr.split(",");
        float[] values = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Float.parseFloat(parts[i]);
        }

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
