
package com.quartey.jobapplicationtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class RestClientConfig {

    @Value("${app.resume-parser.base-url}")
    private String resumeParserBaseUrl;

    @Bean
    public RestClient resumeParserRestClient() {
        return RestClient.builder()
                .baseUrl(resumeParserBaseUrl)
                .requestFactory(new SimpleClientHttpRequestFactory()) //Switching to simple client resolved empty body issue
                .requestInterceptor((request, body, execution) -> {

                    System.out.println("Outgoing Content-Type: "
                            + request.getHeaders().getContentType());

                    System.out.println("Outgoing body size: "
                            + body.length + " bytes");

                    return execution.execute(request, body);
                })
                .build();
    }
}
