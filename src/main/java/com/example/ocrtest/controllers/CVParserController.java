package com.example.ocrtest.controllers;


import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.CV;
import com.example.ocrtest.services.CVParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/cv-parser")
@RequiredArgsConstructor
@Validated

public class CVParserController {

    private final CVParserService cvParserService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ContentResponseDTO> classify(@Valid @NotNull @RequestParam("file") final MultipartFile pdfFile) {
        //CV cv = this.cvParserService.
        return ResponseEntity.ok().body(ContentResponseDTO.builder().content(this.cvParserService.parse(pdfFile)).build());
    }


}