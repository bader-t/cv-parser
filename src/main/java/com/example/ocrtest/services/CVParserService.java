package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CVParserService
{
    ContentResponseDTO parse(final MultipartFile multipartFile) throws Exception;
    String extractContent(final MultipartFile multipartFile);
}
