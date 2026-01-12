package com.artur.vocab.wordlearningapp;

import com.artur.vocab.wordlearningapp.config.property.OpenAiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenAiProperties.class)
public class WordLearningAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordLearningAppApplication.class, args);
    }

}
