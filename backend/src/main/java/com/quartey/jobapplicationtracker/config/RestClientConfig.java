
package com.quartey.jobapplicationtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${app.resume-parser.base-url}")
    private String resumeParserBaseUrl;

    @Bean
    public RestClient resumeParserRestClient() {
        return RestClient.builder()
                .baseUrl(resumeParserBaseUrl)
                .requestInterceptor((request, body, execution) -> {

                    System.out.println("Outgoing Content-Type: "
                            + request.getHeaders().getContentType());

                    String bodyText = new String(
                            body,
                            java.nio.charset.StandardCharsets.ISO_8859_1
                    );

                    int headerEnd = bodyText.indexOf("\r\n\r\n");

                    if (headerEnd != -1) {
                        System.out.println("Multipart headers:");
                        System.out.println(bodyText.substring(0, headerEnd));
                    } else {
                        System.out.println("Could not find multipart header separator.");
                    }

                    System.out.println("First multipart line: "
                            + bodyText.substring(0, bodyText.indexOf("\r\n")));


                    String boundary = request.getHeaders()
                            .getContentType()
                            .getParameter("boundary");

                    System.out.println("Request Content-Length: "
                            + request.getHeaders().getContentLength());

                    System.out.println("Expected closing boundary present: "
                            + bodyText.contains("--" + boundary + "--"));

                    System.out.println("Last 100 multipart characters: "
                            + bodyText.substring(Math.max(0, bodyText.length() - 100)));



                    System.out.println("Multipart file field present: "
                            + bodyText.contains("name=\"file\""));

                    System.out.println("Outgoing body size: "
                            + body.length + " bytes");

                    return execution.execute(request, body);
                })
                .build();
    }
}
