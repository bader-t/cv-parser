package com.example.ocrtest.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CV {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private List<Skill> skills = new ArrayList<>();
    private String education;
    private List<Experience> experiences;
    private List<Certification> certifications = new ArrayList<>();
    private List<String> langues;
    private String linkedin;
    public String toString(){
        return "name :" +this.firstName;
    }
    public void addskill(Skill skill){
        this.skills.add(skill);
    }
}
