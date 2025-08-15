package com.example.gaehwa2.entity;

import jakarta.persistence.*;
import lombok.*;
//import org.hibernate.annotations.Type;
//import com.pgvector.hibernate.VectorType;


import java.time.LocalDate;

@Entity
@Table(name = "flower")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 누가?
    private String flowerFrom;

    // 누구에게?
    private String flowerTo;

    // 관계
    private String relation;

    // 무슨 날?
    private String anniversary;

    // 선물 예정일
    private LocalDate anvDate;

    // 히스토리
    @Column(columnDefinition = "TEXT")
    private String history;

    // 카드 이미지
    @Lob
    private byte[] cardImage;

    // 카드 음성
    @Lob
    private byte[] cardVoice;

//    @Column(columnDefinition = "vector(3)")
//    @Type(VectorType.class)
//    private float[] recommendRgbVector;

    @Column(columnDefinition = "TEXT")
    private String recommendMessage;
}

