package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;


public class UserProfile extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private String userID;
    private TextView name_id;
    private EditText email_id, number_id, address_id;
    private ImageView photoUrl_id;
    private String photoPath;
    private FirebaseAuth auth;
    private ImageButton edit;
    StorageReference storageReference;
    //visibility
    private MaterialCardView card1;
    private MaterialCardView card2;
    private MaterialCardView card3;
    private MaterialCardView card4;
    private MaterialCardView card5;
    private MaterialCardView card6;
    private ImageView backdrop;
    private ImageButton settingsImage;
    private ImageButton historyImage;
    private TextView saveText;
    private TextView historyText;
    private TextView settingsText;

    private String user_ID;
private String personID;



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

        //visibility
        card1 = findViewById(R.id.materialCardView2);
        card2 = findViewById(R.id.materialCardView);
        card3 = findViewById(R.id.materialCardView3);
        /*card4 = findViewById(R.id.imageButtonEdit);
        card5 = findViewById(R.id.imageButtonEdit);
        card6 = findViewById(R.id.imageButtonEdit);*/
        backdrop = findViewById(R.id.imageView10);
        edit = findViewById(R.id.imageButtonEdit);
        settingsImage = findViewById(R.id.imageButtonEdit);
        historyImage = findViewById(R.id.imageButtonEdit);
        saveText = findViewById(R.id.textViewEdit_ID);
        historyText = findViewById(R.id.textViewHistory_ID);
        settingsText = findViewById(R.id.textViewSettings_ID);
        photoUrl_id = findViewById(R.id.userProfilePicture_ID);



        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String userID2 = intent.getStringExtra("userID");

        Intent intent2 = getIntent();
       user_ID = intent2.getStringExtra("yourUserID");

        /*Intent callerIntent = getIntent();
        Bundle packageFromCaller=
                callerIntent.getBundleExtra("Person");
        String personID =packageFromCaller.getString("yourUserID2");
*/
        if (getIntent().hasExtra("yourUserID")){
             personID = getIntent().getStringExtra("yourUserID");

        }


        //If user is clicking on their own profile
        if(personID == auth.getCurrentUser().getUid()){
            userID = user_ID;
            locateProfile();
            getRegInfo(userID);
            edit.setOnClickListener(v -> onClickUpdateProfile());
            updateProfile();
            uploadImageToCloud();
        } else {
            userID = userID2;
            card1.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
            card3.setVisibility(View.GONE);
            backdrop.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            settingsImage.setVisibility(View.GONE);
            historyText.setVisibility(View.GONE);
            settingsText.setVisibility(View.GONE);
            saveText.setVisibility(View.GONE);
            email_id.setEnabled(false);
            number_id.setEnabled(false);
            address_id.setEnabled(false);
            photoUrl_id.setClickable(false);
            settingsImage.setVisibility(View.GONE);
            settingsImage.setVisibility(View.GONE);
            locateProfile();
            getRegInfo(userID);
            edit.setOnClickListener(v -> onClickUpdateProfile());
            updateProfile();
            uploadImageToCloud();
        }

    }

    private void locateProfile(){
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
    }


    private void updateProfile(){
        photoUrl_id = findViewById(R.id.userProfilePicture_ID);
        photoUrl_id.setOnClickListener(v -> {
            //check runtime permission
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                //Permission already granted
                pickImageFromGallery();
            }
        });
    }


    private void uploadImageToCloud(){
        //to upload image to cloud
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+userID+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(photoUrl_id);
                photoPath = uri.toString();
                db.collection("users")
                        .document(userID)
                        .update("imageUrl",photoPath);

            }
        });
    }

    private void getRegInfo(String userID) {
        Toast.makeText(UserProfile.this, "Getting profile data", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(UserProfile.this, "Found profile", Toast.LENGTH_SHORT).show();
                            getPicture();

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


    public void onClickUpdateProfile(){
        name_id = findViewById(R.id.username_ID);
        email_id = findViewById(R.id.emailAddress_ID);
        number_id = findViewById(R.id.phoneNumber_ID);
        address_id = findViewById(R.id.homeAddress_ID);
        photoUrl_id = findViewById(R.id.userProfilePicture_ID);

        db.collection("users")
                .document(userID)
                .update("number",number_id.getText().toString(),
                        "email",email_id.getText().toString(),
                        "address",address_id.getText().toString(),
                        "imageUrl",photoPath);
        getPicture();
        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();

    }


    public void getPicture(){
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
    }


    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                pickImageFromGallery();
            } else {
                //permission was denied
                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "requestCode denied...!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set Image to imageView
            Uri imageUri = data.getData();
            //userProfilePic.setImageURI(imageUri);

            uploadImageToFirebase(imageUri);

        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //Upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+userID+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserProfile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(photoUrl_id);
                        photoPath = uri.toString();
                        db.collection("users")
                                .document(userID)
                                .update("imageUrl",photoPath);


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onClickSettings(View view){
        Intent intent = new Intent(UserProfile.this, SettingsActivity.class);
        startActivity(intent);
    }

}