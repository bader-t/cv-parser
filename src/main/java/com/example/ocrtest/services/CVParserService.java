package com.example.ocrtest.services;

import org.springframework.web.multipart.MultipartFile;

public interface CVParserService
{
    public String extractContent(final MultipartFile multipartFile);
}
