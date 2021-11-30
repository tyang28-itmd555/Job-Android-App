package com.example.phase2_1;

/**
 * Created by picojazz on 08/01/2018.
 */

public class Experiences {
    private Long id;
    private String begin;
    private String end;
    private String position;
    private  String company;
    private String about;


    public Experiences() {
    }

    public Experiences(String begin, String end, String position, String company, String about) {
        this.begin = begin;
        this.end = end;
        this.position = position;
        this.company = company;
        this.about = about;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPosition() {
        return position;
    }

    public void setPostion(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


}
