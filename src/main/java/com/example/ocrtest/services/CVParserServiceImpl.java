package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Certification;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.entities.SectionType;
import com.example.ocrtest.services.sectionImpl.CertificationParser;
import com.example.ocrtest.services.sectionImpl.ExperienceParser;
import com.example.ocrtest.services.sectionImpl.EducationParser;
import com.example.ocrtest.services.sectionImpl.PersonalParser;
import com.example.ocrtest.services.sectionImpl.SkillParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;


@Service
@Slf4j
public class CVParserServiceImpl implements CVParserService{


    private final Map<SectionType, Pattern> sectionMap = new HashMap<>();

    public CVParserServiceImpl() {
        this.sectionMap.put(SectionType.Skills,Pattern.compile("Skills|Compétence|expertise|Talents",Pattern.CASE_INSENSITIVE));
        this.sectionMap.put(SectionType.Education,Pattern.compile("Education|Schooling|Learning|Parcours Scolaire",Pattern.CASE_INSENSITIVE));
        this.sectionMap.put(SectionType.Interest,Pattern.compile("Interest|centres d'intérêt",Pattern.CASE_INSENSITIVE));
        this.sectionMap.put(SectionType.Certification,Pattern.compile("Certification|Certificats|Certif",Pattern.CASE_INSENSITIVE));
        this.sectionMap.put(SectionType.Experience,Pattern.compile("Experience|expérience",Pattern.CASE_INSENSITIVE));
    }


    @Override
    public ContentResponseDTO parse(final MultipartFile multipartFile) {
        CV cv = new CV();
        ContentResponseDTO response = new ContentResponseDTO();
        Map<SectionType, SectionParser> parserMap = new HashMap<>();
        parserMap.put(SectionType.Personal, new PersonalParser());
        parserMap.put(SectionType.Certification, new CertificationParser());
        parserMap.put(SectionType.Skills,new SkillParser());
        parserMap.put(SectionType.Experience,new ExperienceParser());
        parserMap.put(SectionType.Education, new EducationParser());

        String[] lines = this.extractContent(multipartFile).split("\n");
        List<Section> sections = this.extractSection(lines);
        for (Section section: sections) {
            System.out.println(section.getType());
            SectionParser sectionParser = parserMap.get(section.getType());
            if (sectionParser != null) {
                sectionParser.parse(section, cv);
            }
            System.out.println(section.getType() + "\n");
            for (String d: section.getContent()
                 ) {
                System.out.println( "+" + d );
            }
            System.out.println("-------------");

        }
        response.setContent(this.extractContent(multipartFile));
        response.setCv(cv);
        return response;
    }


    public String extractContent(final MultipartFile multipartFile){
        String text;
        if(Objects.equals(multipartFile.getContentType(), "application/pdf")) {
            try (final PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
                final PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
            } catch (final Exception ex) {
                text = "Error parsing PDF";
            }
            return text;

        } else if (Objects.equals(multipartFile.getContentType(), "application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            try (XWPFDocument doc = new XWPFDocument(multipartFile.getInputStream())) {
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
                text = xwpfWordExtractor.getText();
            } catch (Exception e) {
                text = "Error parsing this file";
            }
            return text;

        } return null;
    }


    public List<Section> extractSection(String[] lines){
        List<Section> sections = new ArrayList<>();

        int lineIndex = 0;

        // assume first section contains personal infos
        Section personalSection = new Section(SectionType.Personal);
        while(lineIndex < lines.length && this.findSectionType(lines[lineIndex]) == SectionType.Unknown){
            // delete blank lines
            if(!lines[lineIndex].trim().isEmpty()){
                personalSection.getContent().add(lines[lineIndex]);
            }
            lineIndex++;
        }
        sections.add(personalSection);

        // extract rest of the sections
        while(lineIndex < lines.length){
            SectionType sectionType = this.findSectionType(lines[lineIndex]);
            // new section
            if (sectionType != SectionType.Unknown) {
                Section section = new Section(sectionType);
                // assume the next line is content of current section
                while (lineIndex < lines.length - 1 && this.findSectionType(lines[lineIndex + 1]) == SectionType.Unknown) {
                    lineIndex++;
                    if (!lines[lineIndex].trim().isEmpty()) {
                        section.getContent().add(lines[lineIndex]);
                    }
                }
                sections.add(section);
            }
            lineIndex++;
        }
        return sections;
    }


    public SectionType findSectionType(String line){
        if (line.trim().isEmpty()) {
            return SectionType.Unknown;
        }
        for (Map.Entry<SectionType, Pattern> entry : this.sectionMap.entrySet()) {
            if (entry.getValue().matcher(line).find()) {
                return entry.getKey();
            }
        }
        return SectionType.Unknown;

    }
}
