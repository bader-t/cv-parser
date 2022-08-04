package com.example.ocrtest.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    private String company;
    private String period;
    private String position;
    private String subject;
    private String city;
}
