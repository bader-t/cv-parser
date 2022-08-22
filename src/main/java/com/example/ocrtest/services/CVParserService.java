package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.CV;
import org.springframework.web.multipart.MultipartFile;

public interface CVParserService
{
    ContentResponseDTO parse(final String content) throws Exception;
    String extractContent(final MultipartFile multipartFile);
    public ContentResponseDTO parseException(final String content, CV cv);
}
