package com.example.cs4084_project_farm_market;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_WELCOME_MESSAGE = "ie.ul.CS4048-MobileApplicationProject.EXTRA_MESSAGE";

    public static final String TAG = "TAG";
    private TextInputLayout txt_firstname, txt_surname, txt_email, txt_password, txt_reenterPassword, txt_birthday;
    private Button btn_confirm;
    private FirebaseAuth auth;
    private String userID;
    public FirebaseFirestore db;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //Any letter
                    "(?=.*[@#$%&+=])" +     //At least one special character
                    "(?=\\S+$)" +           //No white spaces
                    ".{4,}" +               //At least 4 characters long
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        txt_firstname = findViewById(R.id.txt_firstname);
        txt_surname = findViewById(R.id.txt_lastname);
        txt_email = findViewById(R.id.txt_registerEmail);
        txt_password = findViewById(R.id.txt_registerPassword);
        //txt_reenterPassword = findViewById(R.id.txt_reenterPassword);
        txt_birthday = findViewById(R.id.txt_birthday);
        btn_confirm = findViewById(R.id.btn_registerConfirm);

        Drawable buttonInline = getResources().getDrawable(R.drawable.button);
        btn_confirm.setBackground(buttonInline);
        EditText birthdayEdit = txt_birthday.getEditText();


        birthdayEdit.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(RegisterActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txt_birthday.getEditText().setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
            picker.show();
        });

        btn_confirm.setOnClickListener(v -> confirmDetails(v));

    }

    public void confirmDetails(View view) {
        boolean validation = validateFirstname() && validateSurname() && validateEmail() && validatePassword();

        if (validation) {
            String email = txt_email.getEditText().getText().toString();
            String password = txt_password.getEditText().getText().toString();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);  //Create document linking to GlobalUsers unique ID
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", txt_firstname.getEditText().getText().toString());
                    user.put("lastName", txt_surname.getEditText().getText().toString());
                    user.put("dob", txt_birthday.getEditText().getText().toString());
                    user.put("email", txt_email.getEditText().getText().toString());
                    user.put("imageUrl", "to be found");
                    user.put("address", "tap to enter");
                    user.put("number", "tap to enter");

                    //addding details to firestore



                    //Fail Handler
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User Profile successfully created for " + userID);

                            //"Welcome to FARMPIRE + name "
                            String fname = txt_firstname.getEditText().getText().toString();
                            Intent intent = new Intent(RegisterActivity.this, ProfileSetUp.class);
                            intent.putExtra(EXTRA_WELCOME_MESSAGE,fname);


                            startActivity(intent);
                            finish();
                        }
                    })
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
        }
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

    private boolean validateFirstname() {
        String firstName = txt_firstname.getEditText().getText().toString();

        if (firstName.isEmpty()) {
            txt_firstname.setError("Field can't be empty");
            return false;
        } else {
            txt_firstname.setError(null);
            return true;
        }
    }

    private boolean validateSurname() {
        String surname = txt_surname.getEditText().getText().toString();

        if (surname.isEmpty()) {
            txt_surname.setError("Field can't be empty");
            return false;
        } else {
            txt_surname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = txt_email.getEditText().getText().toString();

        if (email.isEmpty()) {
            txt_email.setError("Field can't be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            txt_email.setError("Please enter a valid email");
            return false;
        } else {
            txt_email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = txt_password.getEditText().getText().toString();

        if (password.isEmpty()) {
            txt_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            txt_password.setError("Password must contain at least 4 characters, with one being a special character");
            return false;
        } else {
            txt_password.setError(null);
            return true;
        }
    }
}


