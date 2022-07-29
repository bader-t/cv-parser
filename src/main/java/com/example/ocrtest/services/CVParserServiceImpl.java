package com.example.ocrtest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@Slf4j
public class CVParserServiceImpl implements CVParserService{

    @Override
    public String extractContent(final MultipartFile multipartFile) {
        String text=null;
        String[] tab = multipartFile.getName().split("\\.");
        if(multipartFile.getContentType().equals("application/pdf")) {
            try (final PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
                final PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
            } catch (final Exception ex) {
                text = "Error parsing PDF";
            }

            return text;
        }else if (multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            try (XWPFDocument doc = new XWPFDocument(multipartFile.getInputStream())) {
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
                text = xwpfWordExtractor.getText();
            } catch (Exception e) {
                text = "Error parsing this file";
            }
            return text;
        } return null;
    }
}
