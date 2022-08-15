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
            for(String line:section.getContent()){
                if(line.contains(":")) {
                    String[] domainskills = line.trim().split(":");
                    System.out.println(line);
                    if(domainskills.length==1){
                        continue;
                    }else{
                        String[] skills = domainskills[1].split(",");
                        for (String skill : skills) {
                            Skill ski = new Skill(skill.trim(),"Programming");
                            cv.addskill(ski);
                        }
                    }
                }else if(!line.contains(":")){
                    String [] skills = line.trim().split(",");
                    for(String skill:skills){
                        Skill sky = new Skill(skill.trim(),"Programming");
                        cv.addskill(sky);
                    }
                }else if(!line.contains(":") && !line.contains(",")){
                    Skill sky = new Skill(line.trim(),"Programming");
                    cv.addskill(sky);
                }else {
                    if(i==0) {
                        domain = line.trim().split(":");
                    }else if(i==1){
                        String [] skills = line.trim().split(",");
                        for(String skill:skills){
                            Skill sky = new Skill(skill.trim(),"Programming");
                            cv.addskill(sky);
                        }
                        i=-1;
                    }
                    i++;
                }
            }
    }
}
