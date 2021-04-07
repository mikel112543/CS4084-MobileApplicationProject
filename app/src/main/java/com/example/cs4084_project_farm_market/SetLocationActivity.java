package com.example.cs4084_project_farm_market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

public class SetLocationActivity extends AppCompatActivity {

    private ImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarSetLocation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Up Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
}