package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;


public class UserProfile extends AppCompatActivity {


    private String userID;
    private TextView name_id;
    private EditText email_id, number_id, address_id;
    private ImageView photoUrl_id;
    private FirebaseAuth auth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference noteRef ;
    private User userModal;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name_id = findViewById(R.id.username_ID);
        email_id = findViewById(R.id.emailAddress_ID);
        number_id = findViewById(R.id.phoneNumber_ID);
        address_id = findViewById(R.id.homeAddress_ID);
        photoUrl_id = findViewById(R.id.userProfilePicture_ID);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        storageRef = FirebaseStorage.getInstance().getReference().child("users/" + userID + "/profile.jpg");
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UserProfile.this, "Found your profile picture", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.userProfilePicture_ID)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, "No Profile picture located, go back to enter", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        getRegInfo();

    }

    private void getRegInfo(){
        noteRef = db.collection("users").document(userID);
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            userModal = documentSnapshot.toObject(User.class);
                            name_id.setText(userModal.getFirstName() + " " + userModal.getLastName());
                            email_id.setText(userModal.getEmail());
                            Picasso.get().load(userModal.getUrl())
                                    .fit()
                                    .centerCrop()
                                    .into(photoUrl_id);
                            number_id.setText(userModal.getNumber());
                            address_id.setText(userModal.getAddress());
                            photoUrl_id.setImageURI(userModal.getUrl());
                        }else{
                            Toast.makeText(UserProfile.this, "nope", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }







}