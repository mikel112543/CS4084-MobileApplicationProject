package com.example.cs4084_project_farm_market;

import com.google.type.DateTime;

import java.util.Date;

public class Listing {
    private String title;
    private String location;
    private String imageUrl;
    private String description;
    private String date;
    private String time;
    private String price;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Listing() {
    }


    public Listing(String title, String location, String imageUrl, String description, String date, String time, String price) {


        this.title = title;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
        this.time = time;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
