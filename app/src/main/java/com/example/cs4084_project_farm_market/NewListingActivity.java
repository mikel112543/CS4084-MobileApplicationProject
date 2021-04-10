package com.example.cs4084_project_farm_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class NewListingActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private ImageView pickedImage;


    private Button uploadPhotoButton, submitButton;
    private TextInputLayout titleInput, priceInput;
    private EditText descriptionInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        titleInput = findViewById(R.id.titleInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        pickedImage = findViewById(R.id.pickedImage);
        uploadPhotoButton.setOnClickListener(uploadPhotoButtonListener);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();

            Picasso.get().load(filePath).into(pickedImage);
        }
    }
}