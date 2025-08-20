// BouquetRepository.java
package com.example.gaehwa2.repository;

import com.example.gaehwa2.entity.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BouquetRepository extends JpaRepository<Bouquet, Long> {

    // PGvector 컬럼끼리 코사인 유사도 기반 유사한 4개 조회
    @Query(value = "SELECT * FROM bouquet b " +
            "ORDER BY b.bouquet_rgb <#> (SELECT flower.recommend_rgb_vector FROM flower WHERE flower.id = :flowerId) ASC " +
            "LIMIT 4",
            nativeQuery = true)
    List<Bouquet> findTop4ByCosineSimilarity(@Param("flowerId") Long flowerId);
}














//package com.example.gaehwa2.repository;
//
//import com.example.gaehwa2.entity.Bouquet;
//import com.pgvector.PGvector;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface BouquetRepository extends JpaRepository<Bouquet, Long> {
//
//    @Query(
//            value = "SELECT * FROM bouquet b " +
//                    "ORDER BY b.bouquet_rgb <=> :queryVector " +
//                    "LIMIT 4",
//            nativeQuery = true
//    )
//    List<Bouquet> findSimilarBouquets(@Param("queryVector") PGvector queryVector);
//}
//
