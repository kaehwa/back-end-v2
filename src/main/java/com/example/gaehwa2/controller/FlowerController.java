package com.example.gaehwa2.controller;

import com.example.gaehwa2.dto.request.BouquetSelectionRequest;
import com.example.gaehwa2.dto.request.FlowerRequestDto;
import com.example.gaehwa2.dto.request.RecommendMessageRequestDto;
import com.example.gaehwa2.dto.response.*;
import com.example.gaehwa2.entity.Bouquet;
import com.example.gaehwa2.entity.Flower;
import com.example.gaehwa2.entity.Medialetter;
import com.example.gaehwa2.repository.BouquetRepository;
import com.example.gaehwa2.repository.FlowerRepository;
import com.example.gaehwa2.repository.MedialetterRepository;
import com.example.gaehwa2.service.BouquetService;
import com.example.gaehwa2.service.FastApiService;
import com.example.gaehwa2.service.FlowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flowers")
@RequiredArgsConstructor
@Tag(name = "꽃 주문 API", description = "꽃 주문 정보, 이미지, 음성 업로드 API")
public class FlowerController {

    private final FlowerService flowerService;
    private final BouquetService bouquetService;

    private final MedialetterRepository medialetterRepository;

    private final FastApiService fastApiService; // FastAPI 호출 + MP3/MP4 다운로드/업로드 처리
    @Autowired
    private BouquetRepository bouquetRepository;

    @Autowired
    private FlowerRepository flowerRepository;



    @PostMapping("/text")
    @Operation(summary = "꽃 주문 정보 저장", description = "누가, 누구에게, 관계, 무슨 날, 선물 예정일, 히스토리를 저장합니다.")
    public ResponseEntity<Flower> saveFlowerText(@RequestBody FlowerRequestDto dto) {
        return ResponseEntity.ok(flowerService.saveFlowerText(dto));
    }

    @PostMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @Operation(summary = "꽃 카드 이미지 업로드(png,jpg,jpeg 혹시나 오류날까봐 확장자 제한, 10mb(error413,크기제한) 걸어놨슴다~)", description = "꽃 ID를 기반으로 카드 이미지를 업로드합니다.")
    public ResponseEntity<MediaUploadResponseDto> saveFlowerImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.getOriginalFilename() == null ||
                !(file.getOriginalFilename().endsWith(".png") ||
                        file.getOriginalFilename().endsWith(".jpg") ||
                        file.getOriginalFilename().endsWith(".jpeg"))) {
            throw new IllegalArgumentException("지원하지 않는 이미지 파일 형식입니다. (.png, .jpg, .jpeg만 가능)");
        }

        return ResponseEntity.ok(flowerService.saveFlowerImage(id, file));
    }

    @PostMapping(value = "/{id}/voice", consumes = "multipart/form-data")
    @Operation(summary = "꽃 카드 음성 업로드(mp3,wav 혹시나 오류날까봐 확장자 제한, 10mb(error413,크기제한) 걸어놨슴다~)", description = "꽃 ID를 기반으로 카드 음성을 업로드합니다.")
    public ResponseEntity<MediaUploadResponseDto> saveFlowerVoice(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.getOriginalFilename() == null ||
                !(file.getOriginalFilename().endsWith(".mp3") ||
                        file.getOriginalFilename().endsWith(".wav"))) {
            throw new IllegalArgumentException("지원하지 않는 음성 파일 형식입니다. (.mp3, .wav만 가능)");
        }

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


    // 확인용: ID 넣으면 이미지/보이스 크기 확인
    @GetMapping("/{id}/check-media")
    @Operation(summary = "/image,/voice api 쏘고 잘들어갔는지 확인용", description = "파일 유무와 크기 확인가능!!")
    public ResponseEntity<Map<String, Object>> checkFiles(@PathVariable Long id) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("꽃 ID를 찾을 수 없습니다."));

        Map<String, Object> result = new HashMap<>();

        // 이미지 확인
        byte[] image = flower.getCardImage();
        if (image != null) {
            result.put("imageExists", true);
            result.put("imageSize", image.length); // 바이트 단위
        } else {
            result.put("imageExists", false);
            result.put("imageSize", 0);
        }

        // 보이스 확인
        byte[] voice = flower.getCardVoice();
        if (voice != null) {
            result.put("voiceExists", true);
            result.put("voiceSize", voice.length); // 바이트 단위
        } else {
            result.put("voiceExists", false);
            result.put("voiceSize", 0);
        }

        return ResponseEntity.ok(result);
    }

    // 유사 부케 조회
    @GetMapping("/{id}/bouquet-similar")
    @Operation(
            summary = "유사 부케 조회",
            description = "주어진 Flower ID를 기준으로 코사인 유사도를 사용하여 가장 유사한 부케 4개를 조회합니다. id는 flowerid입력임",
            responses = {
                    @ApiResponse(responseCode = "200", description = "유사 부케 4개 조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BouquetResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "해당 Flower ID를 찾을 수 없음")
            }
    )
    public List<BouquetResponseDto> getSimilarBouquets(@PathVariable("id") Long flowerId) {
        return bouquetService.getSimilarBouquets(flowerId);
    }

    @Operation(
            summary = "꽃 선택 API",
            description = "특정 Flower에 대해 사용자가 선택한 Bouquet를 저장합니다."
    )
    @PostMapping("/{id}/bouquet-selection")
    public ResponseEntity<Void> selectBouquet(
            @PathVariable Long id,
            @RequestBody BouquetSelectionRequest request
    ) {
        // Flower 조회
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flower not found"));

        // Bouquet 조회
        Bouquet bouquet = bouquetRepository.findById(request.getSelectedBouquetId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bouquet not found"));

        // Flower에 선택된 Bouquet 저장
        flower.setBouquet(bouquet);
        flowerRepository.save(flower);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Medialetter 생성 API",
            description = "Flower의 cardImage와 cardVoice를 기반으로 FastAPI 호출 후 Medialetter를 생성/업데이트 합니다."
    )
    @PostMapping("/{id}/medialetter")
    public ResponseEntity<Void> generateFlowerMedia(@PathVariable Long id) throws IOException {

        // 1. Flower 조회
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flower ID not found"));

        // 2. FastAPI /tts/clone 호출 -> voiceletter 저장
        byte[] voiceletter = fastApiService.callTtsClone(flower.getCardVoice(), flower.getRecommendMessage());

        // 3. FastAPI /oneclick 호출 -> Azure Blob 저장, URL 얻기
        String videoUrl = fastApiService.callOneClick(flower.getCardImage(), flower.getHistory());

        // 4. Medialetter 생성/업데이트
        Medialetter medialetter = flower.getMedialetter();
        if (medialetter == null) {
            medialetter = new Medialetter();
            medialetter.setFlower(flower);
        }
        medialetter.setVoiceletter(voiceletter);
        medialetter.setVideoletterUrl(videoUrl);
        medialetterRepository.save(medialetter);

        flower.setMedialetter(medialetter); // 양방향 연결

        // 5. 성공만 알려주면 됨
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Flower Medialetter 조회 API",
            description = "특정 Flower의 Medialetter 정보를 조회합니다."
    )
    @GetMapping("/{id}/medialetter")
    public ResponseEntity<FlowerMediaResponseDto> getFlowerMedia(@PathVariable Long id) {
        return ResponseEntity.ok(flowerService.getFlowerMedia(id));
    }

    @GetMapping("/{id}/from-to")
    @Operation(summary = "보내는이/받는이 조회", description = "꽃을 보내는 사람과 받는 사람 정보를 불러옵니다")
    public ResponseEntity<FlowerFromToResponseDto> getFlowerFromTo(@PathVariable Long id) {
        FlowerFromToResponseDto response = flowerService.getFlowerFromTo(id);
        return ResponseEntity.ok(response);
    }


}

