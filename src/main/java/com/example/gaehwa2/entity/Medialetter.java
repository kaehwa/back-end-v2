package com.example.gaehwa2.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "medialetter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medialetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoletterUrl;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "voiceletter", nullable = true)
    private byte[] voiceletter;


    // Flower와 양방향 1:1 (Medialetter가 연관관계의 주인)
    @OneToOne // 변경됨
    @JoinColumn(name = "flower_id") // FK 컬럼 생성됨
    @JsonBackReference
    private Flower flower; // 변경됨

}
