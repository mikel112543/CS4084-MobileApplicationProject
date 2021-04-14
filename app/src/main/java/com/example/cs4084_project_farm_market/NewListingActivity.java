package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class NewListingActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    private static final String TAG = "TAG";

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask urlTask;
    private Uri imageUri;
    private ImageView pickedImage;
    private String userID;
    private String generatedFilePath;


    private Button uploadPhotoButton, submitButton;
    private TextInputLayout titleInput, priceInput;
    private EditText descriptionInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = storage.getReference("listings");

        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        titleInput = findViewById(R.id.titleInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        pickedImage = findViewById(R.id.pickedImage);
        submitButton = findViewById(R.id.submitButton);

        uploadPhotoButton.setOnClickListener(uploadPhotoButtonListener);
        submitButton.setOnClickListener(submitButtonListener);

    }

    //Onclick for gallery
    private final View.OnClickListener uploadPhotoButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Open gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
    };

    //Validate empty fields
    private boolean validateFields() {
        String title = titleInput.getEditText().getText().toString();
        String price = priceInput.getEditText().getText().toString();
        String description = descriptionInput.getText().toString();

        if (title.isEmpty()) {
            titleInput.setError("Please enter a title");
            return false;
        } else if (price.isEmpty()) {
            priceInput.setError("Please enter a price");
            return false;
        } else if (description.isEmpty()) {
            descriptionInput.setError("Please enter a description");
            return false;
        } else {
            return true;
        }
    }

    private final View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (urlTask != null && urlTask.isInProgress()) {
                Toast.makeText(NewListingActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();   //Eliminate the ability to spam the submit button
            } else if (validateFields()) {
                uploadImage();  //Function to upload listing to Firestore
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK      //Function to confirm selection of image.
                && data != null
                && data.getData() != null) {

            imageUri = data.getData();
            //Using Picasso Dependency to handle display of photo by user.
            Picasso.get().load(imageUri).into(pickedImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();      //Retrieve file extension to append to image
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void saveListingToDB() {

        userID = auth.getCurrentUser().getUid();
        String date = LocalDate.now().toString();
        String time = LocalTime.now().toString();
        DocumentReference documentReference = db.collection("listings").document();
        Map<String, Object> listing = new HashMap<>();
        listing.put("userID", userID);
        listing.put("imageUrl", generatedFilePath);
        listing.put("title", titleInput.getEditText().getText().toString());
        listing.put("description", descriptionInput.getText().toString());
        listing.put("price", priceInput.getEditText().getText().toString());
        listing.put("date", date);
        listing.put("time", time);
        documentReference.set(listing).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {     //Success Listener to add Listing to db
                Toast.makeText(NewListingActivity.this, "Listing has been successfully created", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {     //Fail Listener if listing could not be added to db
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewListingActivity.this, "Listing could not be created", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            urlTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    generatedFilePath = uri.toString();         //Task is Asynchronous so Success Listener must be created.
                                    saveListingToDB();
                                }
                            }).addOnFailureListener(new OnFailureListener() {       //Fail Listnere for Download Url
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error retreiving image url", e);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {     //Fail Listener for image upload.
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}