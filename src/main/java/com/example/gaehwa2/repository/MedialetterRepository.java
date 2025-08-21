package com.example.gaehwa2.repository;

import com.example.gaehwa2.entity.Medialetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedialetterRepository extends JpaRepository<Medialetter, Long> {

    // 특정 Flower와 연결된 Medialetter 찾기
    Optional<Medialetter> findByFlowerId(Long flowerId);
}

