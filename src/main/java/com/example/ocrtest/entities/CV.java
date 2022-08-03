package com.example.ocrtest.entities;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class CV {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private List<String> skills;
    private List<String> educations;
    private List<String> experiences;
    private List<Certification> certifications;
    public String toString(){
        return "name :" +this.firstName;
    }
}
