package com.example.gaehwa2.dto.response;

public record MediaUploadResponseDto(
        Long id,
        String flowerTo,
        String flowerFrom
) {
    public static MediaUploadResponseDto fromEntity(com.example.gaehwa2.entity.Flower flower) {
        return new MediaUploadResponseDto(
                flower.getId(),
                flower.getFlowerTo(),
                flower.getFlowerFrom()
        );
    }
}


