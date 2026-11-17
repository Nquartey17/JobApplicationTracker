package com.quartey.jobapplicationtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${app.resume-parser.base-url}")
    private String resumeParserBaseUrl;

    // Connect Python FastAPI to Spring Boot
    @Bean // Registers the client as a Spring managed object, we can inject other java classes  because of this
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(resumeParserBaseUrl)
                .build();
    }
}