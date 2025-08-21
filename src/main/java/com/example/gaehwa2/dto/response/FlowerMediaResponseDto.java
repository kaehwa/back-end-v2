package com.example.gaehwa2.dto.response;

import com.pgvector.PGvector;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlowerMediaResponseDto {
    private Long flowerId;
    private String recommendMessage;
    private String bouquetVideoUrl;
    private PGvector bouquetRgb;
    private byte[] voiceletter;
    private String videoletterUrl;
}

