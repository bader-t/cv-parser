package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.services.SectionParser;

import java.util.regex.Pattern;

public class PersonalParser extends SectionParser {

    @Override
    public void parse(Section section, CV cv){
        String emailregex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phoneregex = "(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}";
        Pattern emailpattern = Pattern.compile(emailregex);
        Pattern phonepattern = Pattern.compile(phoneregex);
        int i=0;
        for(String line:section.getContent()){
            if(i==0){
                String [] firstlast = line.trim().split(" ");
                cv.setFirstName(firstlast[1]);
                cv.setLastName(firstlast[0]);
                System.out.println(cv.getFirstName());
            }else if(emailpattern.matcher(line.trim()).find()){
                cv.setEmail(line.trim());
                System.out.println(cv.getEmail());
            }else if(phonepattern.matcher(line.trim()).find()){
                cv.setPhoneNumber(line.trim());
                System.out.println(cv.getPhoneNumber());
            }else{
                cv.setAddress(line.trim());
                System.out.println(cv.getAddress().trim());
            }
            ++i;
        }
        System.out.println(cv.toString());
    }
}
