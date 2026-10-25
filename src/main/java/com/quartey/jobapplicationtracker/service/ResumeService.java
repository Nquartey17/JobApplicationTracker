package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.ResumeResponse;
import com.quartey.jobapplicationtracker.entity.Resume;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.ResumeRepository;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final Path storageDirectory;


    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository,
                         @Value("${app.upload.resume-dir:uploads/resumes}") //Using Spring Value, not lombok
                         String uploadDirectory) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.storageDirectory = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();
    }

    @Transactional
    public ResumeResponse uploadResume(String name,
                                       Integer version,
                                       MultipartFile file,
                                       JwtAuthenticationToken authentication) {

        String extension = getString(name, version, file);

        boolean validContentType = isValidContentType(file, extension);

        if (!validContentType) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File type does not match the allowed formats (PDF or DOCX)");
        }

        String username = authentication.getToken().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // UUID used for stored filename so two uploads with the same name won't overwrite each other
        // Generate a unique filename (randomly generated identifier and appends extension)
        String storedFileName = UUID.randomUUID() + extension;
        // Constructs the full path where the file should be saved
        // storageDirectory.resolve(storedFileName) combines the directory path with the filename.
        // .normalize() cleans up unnecessary path elements such as . and ..
        Path target = storageDirectory.resolve(storedFileName).normalize();

        // If the target path is outside the designated resume storage directory, reject the operation
        if (!target.startsWith(storageDirectory)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid file path");
        }

        try {
            Files.createDirectories(storageDirectory); // create the resume storage directory if it doesn't already exist
            file.transferTo(target); // save uploaded file
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not store resume file");
            // Can fail because disk is full, filesystem unavailable, no permission to write to directory, etc.
        }

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setName(name.trim());
        resume.setVersion(version == null ? 1 : version);
        resume.setFilePath(target.toString());

        Resume savedResume = resumeRepository.save(resume);

        return new ResumeResponse(
                savedResume.getId(),
                savedResume.getName(),
                savedResume.getVersion(),
                savedResume.getCreatedAt(),
                savedResume.getUpdatedAt()
        );
    }

    private static boolean isValidContentType(MultipartFile file, String extension) {
        if (!extension.equals(".pdf") && !extension.equals(".docx")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only PDF and DOCX files are supported");
        }

        String contentType= file.getContentType();

        return (extension.equals(".pdf")
        && "application/pdf".equals(contentType))
                || (extension.equals(".docx")
        && "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equals(contentType));
    }

    private static @NonNull String getString(String name, Integer version, MultipartFile file) {
        if (name == null || name.isBlank() || name.length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Resume name can't be empty or over 255 characters");
        }

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A resume file is required");
        }

        if (file.getSize() > 5L * 1024 * 1024) {
            throw new ResponseStatusException(
                    HttpStatus.CONTENT_TOO_LARGE, "Resume file must not exceed 5 MB");
        }

        if (version != null && version < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Resume version must be at least 1");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";

        // Find index of the dot and get the text after as the file extension
        if (originalName != null) {
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalName.substring(dotIndex)
                        .toLowerCase(Locale.ROOT);
            }
        }
        return extension;
    }
}
