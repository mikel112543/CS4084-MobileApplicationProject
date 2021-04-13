package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class SecurityActivity extends AppCompatActivity {

    //UI Views
    private TextView authStatus;
    private Button authButton;

    private Executor executor;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        Toolbar toolbar = findViewById(R.id.toolbarSecurity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Security");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init UI views
        authStatus = findViewById(R.id.textView5);
        authButton = findViewById(R.id.buttonAuth);

        //init bio metrics
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SecurityActivity.this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //error authenticating stop tasks that require authentication
                authStatus.setText("Authentication error: " + errString);
                Toast.makeText(SecurityActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //authentication succeeded, continue tasks that require authentication
                authStatus.setText("Authentication succees...!");
                Toast.makeText(SecurityActivity.this, "Authentication succees...!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //failed authentication, stop tasks that require authentication
                authStatus.setText("Authentication failed...!");
                Toast.makeText(SecurityActivity.this, "Authentication failed...!", Toast.LENGTH_SHORT).show();

            }
        });

        //setup title,description on auth dialog
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
                .setNegativeButtonText("User App Password")
                .build();

        //handle authentication button, start authentication
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

    }

    //Continue to Set Location
    public void onClickProfileActivity(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }
}

