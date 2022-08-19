package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Certification;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.helpers.DateParser;
import com.example.ocrtest.services.SectionParser;

import java.text.ParseException;
import java.util.Arrays;

public class CertificationParser extends SectionParser {
    @Override
    public void parse(Section section, CV cv) throws Exception{
            for (String line : section.getContent()) {
                Certification certification = new Certification();
                String[] details = line.trim().split("\\s*-\\s*",2);
                System.out.println(">>" + Arrays.toString(details));
                certification.setName(details[0]);
                DateParser dateParser = new DateParser();
                certification.setDateOfObtention(dateParser.parseDate(details[1]));
                cv.getCertifications().add(certification);
            }
    }
}
