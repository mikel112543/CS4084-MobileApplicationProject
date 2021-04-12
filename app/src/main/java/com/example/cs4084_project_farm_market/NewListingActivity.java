package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
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

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private Uri imageUri;
    private ImageView pickedImage;
    private String userID;
    private Listing listingModel;


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
            if (validateFields()) {
                uploadImage();
            }

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imageUri = data.getData();
            //Using Picasso Dependency to handle upload of photo by user.
            Picasso.get().load(imageUri).into(pickedImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            /*userID = auth.getCurrentUser().getUid();*/
                            String pattern = "dd-MM-yyyy";
                            String date = LocalDate.now().toString();
                            String time = LocalTime.now().toString();
                            String location = "Offaly";

                            /*listingModel.setTitle(titleInput.getEditText().getText().toString());
                            listingModel.setLocation(location);
                            listingModel.setImageUrl(storageReference.getDownloadUrl().toString());
                            listingModel.setDescription(descriptionInput.getText().toString());
                            listingModel.setDate(date);
                            listingModel.setTime(time);
                            listingModel.setPrice(priceInput.getEditText().getText().toString());*/

                            Map<String, Object> listing = new HashMap<>();
                            listing.put("userID", "userID");
                            listing.put("imageUri", storageReference.getDownloadUrl().toString());
                            listing.put("title", titleInput.getEditText().getText().toString());
                            listing.put("description", descriptionInput.getText().toString());
                            listing.put("price", priceInput.getEditText().getText().toString());
                            listing.put("date", date);
                            listing.put("time", time);
                            /* listing.put("model", listingModel);*/

                            db.collection("listings")
                                    .add(listing)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(NewListingActivity.this, "Listing has been successfully created", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(NewListingActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(NewListingActivity.this, "Listing could not be created", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
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