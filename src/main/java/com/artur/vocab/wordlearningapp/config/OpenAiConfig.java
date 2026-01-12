package com.artur.vocab.wordlearningapp.config;

import com.artur.vocab.wordlearningapp.config.property.OpenAiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAiConfig {

    @Bean
    public RestClient openAiRestClient(OpenAiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.apiUrl())
                .defaultHeader("Authorization", "Bearer " + properties.apiKey())
                .defaultHeader("Content-Type", "application/json")
                .requestFactory(new SimpleClientHttpRequestFactory() {{
                    setConnectTimeout(properties.connectTimeout());
                    setReadTimeout(properties.readTimeout());
                }})
                .build();
    }
}