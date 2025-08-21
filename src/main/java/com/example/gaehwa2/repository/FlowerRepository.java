package com.example.gaehwa2.repository;

import com.example.gaehwa2.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlowerRepository extends JpaRepository<Flower, Long> {

    @Query("SELECT f FROM Flower f " +
            "LEFT JOIN FETCH f.medialetter " +
            "LEFT JOIN FETCH f.bouquet " +
            "WHERE f.id = :id")
    Optional<Flower> findByIdWithMedialetterAndBouquet(@Param("id") Long id);
}
