package com.artur.vocab.wordlearningapp.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "openai")
public record OpenAiProperties(
        String apiKey,
        String apiUrl,
        String model,
        Duration connectTimeout,
        Duration readTimeout
) {
}
