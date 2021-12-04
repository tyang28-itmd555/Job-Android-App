package com.example.phase2_1;


//import java.util.List;


public class Offer {

    private String id;
    private String title;
    private String company;
    private String about;
    private String place;
    private double lon;
    private double lat;
    private String contract;
    private int Salary;
    private String dateCreate;

    //private List<Tags> tags;

    public Offer() {
    }


    public Offer(String about, String place, double lon, double lat, String contract, int salary, String dateCreate, String company) {
        this.about = about;
        this.place = place;
        this.lon = lon;
        this.lat = lat;
        this.contract = contract;
        Salary = salary;
        this.dateCreate = dateCreate;
        this.company = company;
    }


    public Offer(String id, String title, String place, String contract, String dateCreate, String company) {
        this.title = title;
        this.id = id;
        this.place = place;
        this.contract = contract;
        this.dateCreate = dateCreate;
        this.company = company;
    }

    public Offer(String id, String title, String place,String company) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    /*public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }*/


}
