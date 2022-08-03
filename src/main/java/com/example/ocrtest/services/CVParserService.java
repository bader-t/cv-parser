package com.example.ocrtest.services;

import org.springframework.web.multipart.MultipartFile;

public interface CVParserService
{
    String parse(final MultipartFile multipartFile);
}
