package com.example.gaehwa2.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendMessageResponseDto {
    private Long id;
    private String recommendMessage;
}
