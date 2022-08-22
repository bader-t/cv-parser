package com.example.ocrtest.controllers;


import com.example.ocrtest.DTOs.ContentResponseDTO;
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
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/cv-parser")
@RequiredArgsConstructor
@Validated
public class CVParserController {

    private final CVParserService cvParserService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ContentResponseDTO> classify(@Valid @NotNull @RequestParam("file") final MultipartFile pdfFile) {
        ContentResponseDTO temp = new ContentResponseDTO();
        if(pdfFile!=null){
            String fileName = Objects.requireNonNull(pdfFile.getOriginalFilename()).split("\\.",2)[0];
            if (fileName != null){
                if(fileName.contains("_")){
                    String[] fullName = fileName.split("_", 2);
                    temp.getCv().setFirstName(fullName[0]);
                    temp.getCv().setLastName(fullName[1]);
                }else {
                    temp.getCv().setFirstName(fileName);
                    temp.getCv().setLastName(fileName);
                }
            }
            try{
                temp = this.cvParserService.parse(pdfFile);
                return ResponseEntity.ok().body(temp);
            }catch (Exception e) {
                return ResponseEntity.ok().body(this.cvParserService.parseException(this.cvParserService.extractContent(pdfFile), temp.getCv()));
            }
        }else{
            throw new IllegalStateException("Please Upload a file");
        }
    }


}