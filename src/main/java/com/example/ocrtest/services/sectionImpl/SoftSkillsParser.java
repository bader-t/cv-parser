package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.entities.Skill;
import com.example.ocrtest.services.SectionParser;

public class SoftSkillsParser extends SectionParser {
    @Override
    public void parse(Section section, CV cv) throws Exception{
            for (String line:section.getContent()){
                Skill skill = new Skill(line.trim(),"Soft Skill");
                cv.addskill(skill);
            }
    }
}
