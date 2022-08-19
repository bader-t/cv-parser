package com.example.ocrtest.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Skill {
    private String name;
    private String type;
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof Skill)
        {
            Skill temp = (Skill) obj;
            return this.name.equalsIgnoreCase(temp.name) && this.type.equalsIgnoreCase(temp.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return (this.name.toLowerCase().hashCode() + this.type.toLowerCase().hashCode());
    }
}
