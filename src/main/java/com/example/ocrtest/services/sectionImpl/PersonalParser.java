package com.example.ocrtest.services.sectionImpl;

import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Section;
import com.example.ocrtest.services.SectionParser;

import java.util.regex.Pattern;

public class PersonalParser extends SectionParser {

    @Override
    public void parse(Section section, CV cv) throws Exception{
            String emailregex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            String phoneregex = "(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}";
            Pattern emailpattern = Pattern.compile(emailregex);
            Pattern phonepattern = Pattern.compile(phoneregex);
            int i = 0;
            for (String line : section.getContent()) {
                if (emailpattern.matcher(line.trim()).find()) {
                    cv.setEmail(line.trim());
                } else if (phonepattern.matcher(line.trim()).find()) {
                    cv.setPhoneNumber(line.trim());
                } else if (i == 0 || i == 1) {
                    String[] firstlast = line.trim().split(" ");
                    if (firstlast.length > 1) {
                        if (firstlast[0].trim().length() > 1) {
                            cv.setFirstName(firstlast[1].trim());
                            cv.setLastName(firstlast[0].trim());
                        } else if (firstlast[0].trim().length() == 1) {
                            firstlast = line.trim().split("  ");
                            cv.setFirstName(firstlast[1].trim());
                            cv.setLastName(firstlast[0].trim());
                        }
                        i = 5;
                    } else {
                        if (i == 0)
                            cv.setLastName(line.trim());
                        else if (i == 1)
                            cv.setFirstName(line.trim());
                    }
                }
                ++i;
            }
    }
}
