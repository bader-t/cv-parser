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
                String [] domainskills = line.trim().split(":");
                String [] skills = domainskills[1].split(",");
                for(String skill:skills){
                    Skill ski = new Skill(skill.trim(),domainskills[0]);
                    skillslist.add(ski);
                }
            }
            cv.setSkills(skillslist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
