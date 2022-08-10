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
        String [] domain = null;
        int i=0;
        try {
            for(String line:section.getContent()){
                if(line.split(":").length>1) {
                    String[] domainskills = line.trim().split(":");
                    String[] skills = domainskills[1].split(",");
                    for (String skill : skills) {
                        Skill ski = new Skill(skill.trim(), domainskills[0]);
                        skillslist.add(ski);
                    }
                }else {
                    if(i==0) {
                        domain = line.trim().split(":");
                    }else if(i==1){
                        String [] skills = line.trim().split(",");
                        for(String skill:skills){
                            Skill sky = new Skill(skill.trim(),domain[0]);
                            skillslist.add(sky);
                        }
                        i=-1;
                    }
                    i++;
                }
            }
            cv.setSkills(skillslist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
