package com.Aswat.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileUtils {

    public static MultipartFile convertToMultipartFile(byte[] bytes, String fileName) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return new MockMultipartFile(fileName, inputStream);
    }
}
