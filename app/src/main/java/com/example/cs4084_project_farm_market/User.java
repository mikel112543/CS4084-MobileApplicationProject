package com.example.cs4084_project_farm_market;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    private String firstName, lastName, address, email, number, dob;
    private Uri url;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference noteRef ;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public User() {

    }

    public User(String firstName, String lastName, String address, String email, String number, Uri url) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.number = number;
        this.url = url;
        this.dob =dob;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }
}

