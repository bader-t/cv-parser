package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.entities.Skill;
import com.example.ocrtest.services.SectionParser;

import java.util.ArrayList;
import java.util.List;

public class SkillParser extends SectionParser {

    @Override
    public void parse(Section section, CV cv){
        List<Skill> skillslist = new ArrayList<>();
        try {
            for(String line:section.getContent()){
                String [] domainSkills = line.trim().split(":");
                String [] skills = domainSkills[1].split(",");
                for(String skill:skills){
                    Skill s = new Skill();
                    s.setName(skill.trim());
                    s.setType("Programming");
                    skillslist.add(s);
                }
            }
            cv.setSkills(skillslist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
