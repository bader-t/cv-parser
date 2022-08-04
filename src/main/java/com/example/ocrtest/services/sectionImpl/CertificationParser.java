package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Certification;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.services.SectionParser;

public class CertificationParser extends SectionParser {
    @Override
    public void parse(Section section, CV cv){

        for (String line : section.getContent()) {
            Certification certification = new Certification();
            String[] details = line.trim().split("\\s*-\\s*");
            certification.setName(details[0]);
            certification.setStartDate(details[1]);
            cv.getCertifications().add(certification);
        }

    }
}
