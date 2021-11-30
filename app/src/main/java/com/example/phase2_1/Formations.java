package com.example.phase2_1;

/**
 * Created by picojazz on 08/01/2018.
 */

public class Formations {
    private Long id;
    private String date;
    private String name;
    private String school;


    public Formations() {
    }

    public Formations(String date, String name, String school) {
        this.date = date;
        this.name = name;
        this.school = school;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }


}
