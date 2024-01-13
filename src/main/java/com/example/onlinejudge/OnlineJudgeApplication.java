package com.example.onlinejudge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@MapperScan("com.example.onlinejudge.mapper")
public class OnlineJudgeApplication {

    public static void main(String[] args) {
        String basePath = "./judge/cpp";
        if (!Files.exists(Path.of(basePath))) {
            try {
                Files.createDirectories(Path.of(basePath));
                Files.createDirectories(Path.of(basePath + "/ret"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        SpringApplication.run(OnlineJudgeApplication.class, args);
    }

}
