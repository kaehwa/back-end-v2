package com.example.gaehwa2.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FastApiRecommendResponseDto {
    private List<List<Integer>> rgb_selected;
    private String message;
}