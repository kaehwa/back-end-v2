package com.example.gaehwa2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY) // bytea에 대응
    @Column(name = "card_image", nullable = true)
    private byte[] cardImage;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "card_voice", nullable = true)
    private byte[] cardVoice;

    @Column(columnDefinition = "vector(15)")
    @Convert(converter = VectorConverter.class)
    private float[] recommendRgbVector;

    @Column(columnDefinition = "TEXT")
    private String recommendMessage;
}

