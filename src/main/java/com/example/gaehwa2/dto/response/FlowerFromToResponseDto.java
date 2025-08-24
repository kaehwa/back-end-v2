package com.example.gaehwa2.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FlowerFromToResponseDto {
    private Long id;
    private String flowerFrom;
    private String flowerTo;
    private String anniversary;
}

