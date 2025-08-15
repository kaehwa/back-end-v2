package com.example.gaehwa2.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FlowerRequestDto {

    @Schema(description = "누가?", example = "홍길동")
    private String flowerFrom;

    @Schema(description = "누구에게?", example = "홍길동")
    private String flowerTo;

    @Schema(description = "관계", example = "친구")
    private String relation;

    @Schema(description = "무슨 날", example = "생일")
    private String anniversary;

    @Schema(description = "선물 예정일", example = "2025-08-20")
    private LocalDate anvDate;

    @Schema(description = "히스토리(텍스트)", example = "고등학교 시절부터 친구입니다.")
    private String history;
}
