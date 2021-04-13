package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Address;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetLocationActivity extends AppCompatActivity {

    private ImageView profilePicture;

    public static final double LONGITUDE = 1001;
    public static final double LATITUDE = 1000;
    FirebaseAuth fAuth ;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseAuth auth;


    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private double latitude;
    private double longitude;
    StorageReference storageReference;

    //Initialize variable
    private Button btLocation;
    private Button btGmaps;
    public TextView textViewForAddress, addressFromMaps;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Intent intent = getIntent();

        String address = intent.getStringExtra(MapsActivity.ADDRESS);
        TextView textViewAddress = findViewById(R.id.addressFromMaps);
        textViewAddress.setText(address);




        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarSetLocation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Up Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        //getting profile picture from database storage
        userId = fAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference().child("users/" + userId + "/profile.jpg");
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SetLocationActivity.this, "Found your profile picture", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.profilePictureOnLocation)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetLocationActivity.this, "No Profile picture located, go back to enter", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign variable
        btLocation = findViewById(R.id.bt_location);
        textViewForAddress = findViewById(R.id.textViewForAddress);

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (ActivityCompat.checkSelfPermission(SetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission is granted
                    getLocation();

                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(SetLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //initialize geoCoder
                        Geocoder geocoder = new Geocoder(SetLocationActivity.this, Locale.getDefault());
                        //Initialize Address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        //Set Latitude on TextView
                        textViewForAddress.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address :</b><br></font>"
                                        + addresses.get(0).getLatitude() +", "+ addresses.get(0).getLongitude()
                        ));
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();




                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    //Continue to Set google maps
    public void onClickGoogleMaps(View view){
        Intent intent = new Intent(SetLocationActivity.this, MapsActivity.class);
        getLocation();

        intent.putExtra(String.valueOf(LONGITUDE),longitude);
        intent.putExtra(String.valueOf(LATITUDE),latitude);

        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);
        startActivity(intent);
    }

    public void onClickSecurityActivity(View view){
        Intent intent2 = new Intent(SetLocationActivity.this, SecurityActivity.class);

        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        Map<String, Object> user = new HashMap<>();
        addressFromMaps = findViewById(R.id.addressFromMaps);
        user.put("address", addressFromMaps.getText().toString());
        documentReference.set(user);



        startActivity(intent2);


    }

}