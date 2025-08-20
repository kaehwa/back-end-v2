package com.example.gaehwa2.dto.request;


import lombok.Data;

@Data
public class FastApiRecommendRequestDto {
    private String when_text;
    private String actor;
    private String recipient;
    private String relationship;
    private String history;
    private String recipient_gender;
    private int rgb_target;
}
