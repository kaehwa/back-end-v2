package com.example.gaehwa2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Gaehwa2Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // .env 읽기
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD")); // 환경 변수로 설정
        SpringApplication.run(Gaehwa2Application.class, args);
    }
}

