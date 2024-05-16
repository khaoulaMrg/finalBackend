package com.Aswat.services.jwt;

import com.Aswat.services.jwt.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class fileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Prepare the directory for storing the file
        Path directory = Paths.get(uploadDir);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Save the file to the upload directory
        Path filePath = directory.resolve(fileName).normalize();
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}
