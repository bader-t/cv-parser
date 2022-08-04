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
    private List<Skill> skills;
    private List<String> educations;
    private List<Experience> experiences;
    private List<Certification> certifications = new ArrayList<>();;
    public String toString(){
        return "name :" +this.firstName;
    }




}
