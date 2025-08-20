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
//
//@Service
//@RequiredArgsConstructor
//public class BouquetService {
//
//    private final BouquetRepository bouquetRepository;
//
////    public List<BouquetResponseDto> getSimilarBouquets(Long flowerId) {
////        List<Bouquet> bouquets = bouquetRepository.findTop4ByCosineSimilarity(flowerId);
////
////        return bouquets.stream().map(b -> BouquetResponseDto.builder()
////                .id(b.getId())
////                .name(b.getBouquetName())
////                .imageBase64(b.getBouquetImage() != null ? Base64.getEncoder().encodeToString(b.getBouquetImage()) : null)
////                .rgb(b.getBouquetRgb() != null ? convertVectorToIntList(b.getBouquetRgb()) : null)
////                .anniversaryName(b.getAnniversaryName())
////                .emotionName(b.getEmotion())
////                .build()
////        ).collect(Collectors.toList());
////
////    }
//
//    public List<BouquetResponseDto> getSimilarBouquets(Long flowerId) {
//        List<Bouquet> bouquets = bouquetRepository.findTop4ByCosineSimilarity(flowerId);
//
//        return bouquets.stream().map(b -> {
//            String imageBase64 = null;
//            byte[] imageBytes = null;
//
//            Object imageObject = b.getBouquetImage(); // 이미지 객체 가져오기
//
//            if (imageObject != null) {
//                if (imageObject instanceof byte[]) {
//                    imageBytes = (byte[]) imageObject;
//                } else if (imageObject instanceof Byte[]) {
//                    Byte[] byteObjects = (Byte[]) imageObject;
//                    imageBytes = new byte[byteObjects.length];
//                    for (int i = 0; i < byteObjects.length; i++) {
//                        imageBytes[i] = byteObjects[i]; // Byte → byte 변환
//                    }
//                }
//            }
//
//            if (imageBytes != null) {
//                imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
//            }
//
//            return BouquetResponseDto.builder()
//                    .id(b.getId())
//                    .name(b.getBouquetName())
//                    .imageBase64(imageBase64)
//                    .rgb(b.getBouquetRgb() != null ? convertVectorToIntList(b.getBouquetRgb()) : null)
//                    .anniversaryName(b.getAnniversaryName())
//                    .emotionName(b.getEmotion())
//                    .build();
//        }).collect(Collectors.toList());
//    }
//
//
//
//
//
//
//    private List<List<Integer>> convertVectorToIntList(com.pgvector.PGvector vector) {
//        List<List<Integer>> rgbList = new java.util.ArrayList<>();
//        float[] values = vector.toArray(); // PGvector를 float 배열로 변환
//        for (int i = 0; i < values.length; i += 3) {
//            rgbList.add(List.of(
//                    Math.round(values[i]),
//                    Math.round(values[i + 1]),
//                    Math.round(values[i + 2])
//            ));
//        }
//        return rgbList;
//    }
//}


@Service
@RequiredArgsConstructor
public class BouquetService {

    private final BouquetRepository bouquetRepository;

    public List<BouquetResponseDto> getSimilarBouquets(Long flowerId) {
        List<Bouquet> bouquets = bouquetRepository.findTop4ByCosineSimilarity(flowerId);

        return bouquets.stream().map(b -> {
            String imageBase64 = null;
            byte[] imageBytes = b.getBouquetImage(); // 엔티티 필드 타입이 byte[] 라고 가정

            if (imageBytes != null && imageBytes.length > 0) {
                // JPEG/PNG 등 mime 추정해서 data: prefix 포함 (없어도 되지만 프론트 편의상 추천)
                String mime = guessMime(imageBytes); // 아래 헬퍼 참고
                imageBase64 = "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
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

    private String guessMime(byte[] bytes) {
        // FF D8 FF => JPEG, 89 50 4E 47 => PNG, 47 49 46 38 => GIF, 그 외는 octet-stream
        if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8 && (bytes[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        if (bytes.length >= 4 && (bytes[0] & 0xFF) == 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) {
            return "image/png";
        }
        if (bytes.length >= 4 && bytes[0] == 0x47 && bytes[1] == 0x49 && bytes[2] == 0x46 && bytes[3] == 0x38) {
            return "image/gif";
        }
        return "application/octet-stream";
    }

    private List<List<Integer>> convertVectorToIntList(com.pgvector.PGvector vector) {
        List<List<Integer>> rgbList = new java.util.ArrayList<>();
        float[] values = vector.toArray();
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
