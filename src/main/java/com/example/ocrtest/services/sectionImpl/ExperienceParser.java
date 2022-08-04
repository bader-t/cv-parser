package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Experience;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.services.SectionParser;

import java.util.ArrayList;
import java.util.List;

public class ExperienceParser extends SectionParser {
    @Override
    public void parse(Section section, CV cv){
        int i=0;
        List<Experience> experienceList = new ArrayList<>();
        Experience experience = new Experience();
        for(String line:section.getContent()){
            if(i==0){
                String [] companyperiod = line.trim().split(":");
                experience.setCompany(companyperiod[0].trim());
                experience.setPeriod(companyperiod[1].trim());

            }else if(i==1){
                String [] positioncity = line.trim().split("  ");
                List<String> positioncity2 = new ArrayList<>();
                for (String item:positioncity){
                    if(item.trim()!=""){
                        positioncity2.add(item.trim());
                    }
                }
                experience.setPosition(positioncity2.get(0).trim());
                experience.setCity(positioncity2.get(1).trim());
            }else if(i==2){
                experience.setSubject(line.trim());
                experienceList.add(experience);
                experience = new Experience();
                i=-1;
            }
            i++;
        }
        cv.setExperiences(experienceList);
    }
}
