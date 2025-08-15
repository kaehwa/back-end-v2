package com.example.gaehwa2.controller;

import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.request.RecommendMessageRequestDto;
import com.example.gaehwa2.dto.response.RecommendMessageResponseDto;
import com.example.gaehwa2.entity.Flower;
import com.example.gaehwa2.service.FlowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/flowers")
@RequiredArgsConstructor
@Tag(name = "꽃 주문 API", description = "꽃 주문 정보, 이미지, 음성 업로드 API")
public class FlowerController {

    private final FlowerService flowerService;

    @PostMapping("/text")
    @Operation(summary = "꽃 주문 정보 저장", description = "누가, 누구에게, 관계, 무슨 날, 선물 예정일, 히스토리를 저장합니다.")
    public ResponseEntity<Flower> saveFlowerText(@RequestBody FlowerRequestDto dto) {
        return ResponseEntity.ok(flowerService.saveFlowerText(dto));
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "꽃 카드 이미지 업로드", description = "꽃 ID를 기반으로 카드 이미지를 업로드합니다.")
    public ResponseEntity<Flower> saveFlowerImage(@PathVariable Long id,
                                                  @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(flowerService.saveFlowerImage(id, file));
    }

    @PostMapping("/{id}/voice")
    @Operation(summary = "꽃 카드 음성 업로드", description = "꽃 ID를 기반으로 카드 음성을 업로드합니다.")
    public ResponseEntity<Flower> saveFlowerVoice(@PathVariable Long id,
                                                  @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(flowerService.saveFlowerVoice(id, file));
    }

    // GET /api/flowers/{id}/message
    @GetMapping("/{id}/message")
    @Operation(summary = "추천메세지 조회", description = "정보를 바탕으로 추천된 메세지를 불러옵니다")
    public ResponseEntity<RecommendMessageResponseDto> getRecommendMessage(@PathVariable Long id) {
        String message = flowerService.getRecommendMessage(id);

        RecommendMessageResponseDto responseDto = RecommendMessageResponseDto.builder()
                .id(id)
                .recommendMessage(message)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    // PATCH /api/flowers/{id}/message
    @PatchMapping("/{id}/message")
    @Operation(summary = "추천메세지 수정", description = "정보를 바탕으로 추천된 메세지를 수정 할 수 있습니다.")
    public ResponseEntity<RecommendMessageResponseDto> updateRecommendMessage(
            @PathVariable Long id,
            @RequestBody RecommendMessageRequestDto requestDto) {

        String updatedMessage = flowerService.updateRecommendMessage(id, requestDto.getRecommendMessage());

        RecommendMessageResponseDto responseDto = RecommendMessageResponseDto.builder()
                .id(id)
                .recommendMessage(updatedMessage)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}

