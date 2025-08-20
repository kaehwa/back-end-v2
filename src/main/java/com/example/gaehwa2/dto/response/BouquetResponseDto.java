// BouquetResponseDto.java
package com.example.gaehwa2.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BouquetResponseDto {
    private Long id;
    private String name;               // 부케 이름
    private String imageBase64;        // 이미지 Base64
    private List<List<Integer>> rgb;   // RGB 5개, 리스트 안에 리스트
    private String anniversaryName;    // 기념일명
    private String emotionName;        // 감정명
}
