package com.example.cs4084_project_farm_market;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txt_email, txt_password;
    private FirebaseAuth auth;
    private Button loginButton;
    private Button registerButton;
    private TextView  txt_passReset;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        /*if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));  //Load Main activity if user is logged in
            finish();
        }*/

        setContentView(R.layout.activity_login);

        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        registerButton = findViewById(R.id.btn_register);
        txt_passReset = findViewById(R.id.txt_passReset);
        loginButton = findViewById(R.id.btn_login);

        Drawable buttonInline = getResources().getDrawable(R.drawable.button);

        loginButton.setBackground(buttonInline);
        registerButton.setPadding(10,10,10,10);

        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogin(v);
            }
        });

        txt_passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordResetActivity.class));
            }
        });
    }


    public void confirmLogin(View view) {
        boolean validation = validateEmail() && validatePassword();     //Both methods must return true

        if(validation) {
            String email = txt_email.getEditText().getText().toString();
            String password = txt_password.getEditText().getText().toString();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override   //Calling Firebase login method
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, ("Login Failed!"), Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    public boolean validateEmail() {
        String email = txt_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.isEmpty()) {
            txt_email.setError("Field can't be empty");
            return false;
        }else if(!email.matches(emailPattern)) {
            txt_email.setError("Please enter a valid email");
            return false;
        }else {
            txt_email.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        String password = txt_password.getEditText().getText().toString();

        if(password.isEmpty()) {
            txt_password.setError("Field can't be empty");
            return false;
        }else {
            txt_password.setError(null);
            return true;
        }
    }
}