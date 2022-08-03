package com.example.ocrtest.entities;

import lombok.Data;

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
    private List<String> certifications;



}
