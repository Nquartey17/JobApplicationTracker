package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.ResumeParserResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeParserService {

    private final RestClient resumeParserRestClient;

    public ResumeParserService(RestClient resumeParserRestClient) {
        this.resumeParserRestClient = resumeParserRestClient;
    }

    public ResumeParserResponse parseResume(MultipartFile file) throws IOException {

            String filename = file.getOriginalFilename();

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() { // Convert Multipart file to String for FastAPI
                    return filename;
                }
            };

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("file", resource) // name MUST be "file" to match FastAPI endpoint
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        return resumeParserRestClient.post()
                .uri("/parse") // Send file to /parse
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(bodyBuilder.build())
                .retrieve()
                .body(ResumeParserResponse.class);

    }
}
