package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private TextInputLayout emailReset;
    private Button reset_btn;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        emailReset = findViewById(R.id.passwordReset);
        reset_btn = findViewById(R.id.reset_btn);
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.reset_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendEmail(View view) {
        if(validateEmail()) {
            String email = emailReset.getEditText().getText().toString();

            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(PasswordResetActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PasswordResetActivity.this, "Failed to send password reset email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private boolean validateEmail() {
        String email = emailReset.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.isEmpty()) {
            emailReset.setError("Field can't be empty");
            return false;
        }else if(!email.matches(emailPattern)) {
            emailReset.setError("Please enter a valid email");
            return false;
        }else {
            emailReset.setError(null);
            return true;
        }

    }
}