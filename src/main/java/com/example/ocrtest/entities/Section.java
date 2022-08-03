package com.example.ocrtest.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Section {
    private SectionType type;
    private List<String> content;

    public Section(SectionType type) {
        this.type = type;
        this.content = new ArrayList<>();
    }
}
