package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Certification;
import com.example.ocrtest.entities.Skill;
import com.example.ocrtest.entities.Skill;
import com.example.ocrtest.helpers.DateParser;
import org.apache.xmlbeans.impl.regex.Match;
import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AfterExceptionParsing {
    private final static DateParser dateParser = new DateParser();
    private final static String dateRegex = "[a-z]{3}\\s\\d{4}|[a-z]{4,}\\s\\d{4}|\\d{1,2}-\\d{1,2}-\\d{4}|\\d{1,2}\\s\\d{1,2}\\s\\d{4}|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{1,2}/\\d{1,2}/\\d{4}|\\d{4}/\\d{1,2}/\\d{1,2}|\\d{1,2}\\s[a-z]{3}\\s\\d{4}|\\d{1,2}\\s[a-z]{4,}\\s\\d{4}";
    // "[a-z]{3}\\s\\d{4}|[a-z]{4,}\\s\\d{4}|\\d{1,2}-\\d{1,2}-\\d{4}|\\d{1,2}\\s\\d{1,2}\\s\\d{4}|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{1,2}/\\d{1,2}/\\d{4}|\\d{4}/\\d{1,2}/\\d{1,2}|\\d{1,2}\\s[a-z]{3}\\s\\d{4}|\\d{1,2}\\s[a-z]{4,}\\s\\d{4}";

    public static ContentResponseDTO parseException(final String content, CV cv){
        String hardskilldata = "JAVA";
        String softskilldata = "";
        String certificationdata = "";
        List<Skill> listofskills = new ArrayList<>();
        List<Certification> listofcertification = new ArrayList<>();
        try {
            ClassLoader classLoader = AfterExceptionParsing.class.getClassLoader();
            File file = new File(classLoader.getResource("skillsdataset.txt").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            hardskilldata = br.readLine();
            br.close();
            file = new File(classLoader.getResource("soft_skills.txt").getFile());
            br = new BufferedReader(new FileReader(file));
            softskilldata = br.readLine();
            br.close();
            file = new File(classLoader.getResource("certifications.txt").getFile());
            br = new BufferedReader(new FileReader(file));
            certificationdata = br.readLine();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Pattern skillspattern = Pattern.compile(hardskilldata,Pattern.CASE_INSENSITIVE);
        Pattern softskillspattern = Pattern.compile(softskilldata,Pattern.CASE_INSENSITIVE);
        Pattern certificationpattern = Pattern.compile(certificationdata,Pattern.CASE_INSENSITIVE);
        ContentResponseDTO response = new ContentResponseDTO();
        response.setContent(content);
        String emailregex = "[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        String phoneregex = "(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}";
        Pattern emailpattern = Pattern.compile(emailregex);
        Pattern phonepattern = Pattern.compile(phoneregex);
        Pattern datepattern = Pattern.compile(dateRegex,Pattern.CASE_INSENSITIVE);
        String dateoftoday = "not specified";
        if(cv.getPhoneNumber()==null || cv.getEmail()==null || cv.getSkills()==null){
            int i=0;
            for(String line:content.split("\n")){
                Matcher matcheremail = emailpattern.matcher(line);
                Matcher matcherphone = phonepattern.matcher(line);
                Matcher matcherskills = skillspattern.matcher(line);
                Matcher matchersoftskills = softskillspattern.matcher(line);
                Matcher matchercertification = certificationpattern.matcher(line);
                Matcher matcherdate = datepattern.matcher(line);
                if(matcherphone.find()){
                    cv.setPhoneNumber(matcherphone.group());
                }
                if(matcheremail.find()){
                    cv.setEmail(matcheremail.group());
                }
                if(matcherskills.find()){
                    Skill newskill = null;
                    do {
                        newskill = new Skill(matcherskills.group(), "Programming");
                        listofskills.add(newskill);
                    }while (matcherskills.find());
                }
                if(matchersoftskills.find()){
                    Skill newsoftskill = null;
                    do{
                        newsoftskill = new Skill(matchersoftskills.group(),"Soft Skill");
                        listofskills.add(newsoftskill);
                    }while (matchersoftskills.find());
                }
                if(matchercertification.find()){
                    Certification newsoftskill = null;
                    if(matcherdate.find()){
                        dateoftoday = dateParser.parseDate(matcherdate.group());
                    }
                    do{
                        newsoftskill = new Certification(matchercertification.group(),dateoftoday);
                        listofcertification.add(newsoftskill);
                    }while (matchercertification.find());
                }

            }
            List<Skill> cleanlist = listofskills.stream().distinct().collect(Collectors.toList());
            List<Certification> cleancertificatiolist = listofcertification.stream().distinct().collect(Collectors.toList());
            cv.setSkills(cleanlist);
            cv.setCertifications(cleancertificatiolist);
            response.setCv(cv);
        }
        return response;
    }
}
