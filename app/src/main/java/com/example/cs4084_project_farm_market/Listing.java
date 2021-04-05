package com.example.cs4084_project_farm_market;

import com.google.type.DateTime;

import java.util.Date;

public class Listing {
    private String title;
    private String location;
    private String date;
    private int price;

    public Listing() {
    }

    public Listing(String title, String location, String date, int price) {


        this.title = title;

        this.location = location;
        this.date = date;
        this.price = price;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setPostDateTime(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
