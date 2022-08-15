package com.example.ocrtest.DTOs;

import com.example.ocrtest.entities.CV;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponseDTO {private String content;
    private CV cv;
    private String error;
}

