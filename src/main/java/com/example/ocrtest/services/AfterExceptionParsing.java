package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.CV;
import com.example.ocrtest.entities.Skill;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AfterExceptionParsing {
    public static void main(String [] args){

        try {
            ClassLoader classLoader = AfterExceptionParsing.class.getClassLoader();
            File file = new File(classLoader.getResource("skillsdataset.txt").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String data = br.readLine();
            for(String item:data.split("\\|")){
                System.out.println("\\b"+item+"\\b");
            }
            System.out.println(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ContentResponseDTO parseException(final String content, CV cv){

        String data = "";
        List<Skill> listofskills = new ArrayList<>();
        try {
            ClassLoader classLoader = AfterExceptionParsing.class.getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("skillsdataset.txt")).getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            data = br.readLine();
            System.out.println(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        Pattern skillspattern = Pattern.compile(data,Pattern.CASE_INSENSITIVE);
        ContentResponseDTO response = new ContentResponseDTO();
        response.setContent(content);
        String emailregex = "[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        String phoneregex = "(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}";
        Pattern emailpattern = Pattern.compile(emailregex);
        Pattern phonepattern = Pattern.compile(phoneregex);
        if(cv.getPhoneNumber()==null || cv.getEmail()==null || cv.getSkills()==null){
            for(String line:content.split("\n")){
                Matcher matcheremail = emailpattern.matcher(line);
                Matcher matcherphone = phonepattern.matcher(line);
                Matcher matcherskills = skillspattern.matcher(line);

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

            }
            List<Skill> cleanlist = listofskills.stream().distinct().collect(Collectors.toList());
            cv.setSkills(cleanlist);
            response.setCv(cv);
        }
        return response;
    }
}
