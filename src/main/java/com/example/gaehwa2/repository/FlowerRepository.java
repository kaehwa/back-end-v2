package com.example.gaehwa2.repository;

import com.example.gaehwa2.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
}

