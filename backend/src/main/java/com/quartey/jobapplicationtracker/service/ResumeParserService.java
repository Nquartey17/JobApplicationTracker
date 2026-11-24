package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.ResumeParserResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ContentDisposition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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


        HttpHeaders fileHeaders = new HttpHeaders();

        fileHeaders.setContentDisposition(
                ContentDisposition.formData()
                        .name("file")
                        .filename(filename)
                        .build()
        );

        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<ByteArrayResource> filePart =
                new HttpEntity<>(resource, fileHeaders);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);


        return resumeParserRestClient.post()
                .uri("/parse") // Send file to /parse
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(ResumeParserResponse.class);




    }
}
