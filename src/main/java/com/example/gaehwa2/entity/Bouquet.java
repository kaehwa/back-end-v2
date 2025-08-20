package com.example.gaehwa2.entity;

import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "bouquet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)  // bytea 대응
    private byte[] bouquetImage;

    // RGB 5개 벡터 (15차원)
    @Column(name = "bouquet_rgb", columnDefinition = "vector(15)")
    @Convert(converter = PGvectorConverter.class)
    private PGvector bouquetRgb;

    // 동영상 URL
    @Column(length = 500) // URL 길이 충분히 확보
    private String bouquetVideoUrl;

    // 감정
    @Column(name = "emotion", length = 100)
    private String emotion;

    // 기념일 이름
    @Column(name = "anniversary_name", length = 200)
    private String anniversaryName;

    // 꽃다발 이름
    @Column(name = "bouquet_name", length = 200)
    private String bouquetName;
}

