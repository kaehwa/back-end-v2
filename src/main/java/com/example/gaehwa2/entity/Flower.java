package com.example.gaehwa2.entity;

import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
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

    @Column(name = "recommend_rgb_vector", columnDefinition = "vector(15)")
    @Convert(converter = PGvectorConverter.class)
    private PGvector recommendRgbVector;


    @Column(columnDefinition = "TEXT")
    private String recommendMessage;

    // ----------- 연관관계 ------------

    // Medialetter와 양방향 1:1 (Flower 기준: 주인 아님, mappedBy 사용)
    @OneToOne(mappedBy = "flower", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 변경됨
    private Medialetter medialetter;


    // Bouquet와 다대일 관계 (여러 Flower가 같은 Bouquet를 선택 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bouquet_id")
    private Bouquet bouquet;

}

