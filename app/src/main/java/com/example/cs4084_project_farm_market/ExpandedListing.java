package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ExpandedListing extends AppCompatActivity {


    private static final String TAG = "TAG";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private String documentID;
    private String userID;
    private TextView userName, listingTitle, listingDescription, listingTime, listingDate, listingPrice;
    private Button buyButton;
    private ImageButton profileButton;
    private ImageView listingImage;
    private Listing listing;
    /*private User user;*/
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_expanded_listing);

        listingTitle = findViewById(R.id.expanded_listing_title);
        listingDescription = findViewById(R.id.expanded_listing_description);
        userName = findViewById(R.id.user_name);
        listingTime = findViewById(R.id.expanded_listing_time);
        listingDate = findViewById(R.id.expanded_listing_date);
        listingPrice = findViewById(R.id.expanded_listing_price);
        listingImage = findViewById(R.id.expanded_listing_image);
        buyButton = findViewById(R.id.buy_button);
        profileButton = findViewById(R.id.listing_profile_button);
        collapsingToolbarLayout = findViewById(R.id.listing_collapsing_toolbar);

        /*profileButton.setOnClickListener(profileButtonListener);
        buyButton.setOnClickListener(buyButtonListener);*/

        Intent intent = getIntent();
        documentID = intent.getStringExtra("documentId");
        getListingInfo();
        /*getUserInfo(userID);*/
    }


    /**
     * Retrieve Listing Information by passing the Listing DocumentID in the Intent
     * From There the info can be added to the Listing Modal then can populate the activity
     */
    private void getListingInfo() {
        DocumentReference documentReference = db.collection("listings").document(documentID);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            listing = documentSnapshot.toObject(Listing.class);

                            listingTitle.setText(listing.getTitle());
                            listingDescription.setText(listing.getDescription());
                            listingTime.setText(listing.getTime());
                            listingDate.setText(listing.getDate());
                            listingPrice.setText(String.format("€%s", listing.getPrice()));
                            userID = listing.getUserID();
                            collapsingToolbarLayout.setTitle(listing.getTitle());
                            Picasso.get().load(listing.getImageUrl())
                                    .fit()
                                    .centerCrop()
                                    .into(listingImage);
                            String currentUser = auth.getCurrentUser().getUid();
                            if (userID.equals(currentUser)) {
                                buyButton.setVisibility(View.INVISIBLE);
                            }

                        } else {
                            Toast.makeText(ExpandedListing.this, "Could not find the listing", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ExpandedListing.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());

                    }
                });
    }

    /**
     * @param userID Obtainted from the Listing Document. Used to further populate the activity with User name and address
     */
    private void getUserInfo(String userID) {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            /*user = documentSnapshot.toObject(User.class);
                            /*userName.setText(user.getName()) ;*/
                            //Ready to connect to Bandis Profile Activity

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ExpandedListing.this, "Could not find the user", Toast.LENGTH_SHORT).show();
                    }
                });
        //Document reference to retrieve information of user that created listing
        //Name
        //Location
        //Profile Pic
    }

    /**
     * OnClick to go to profie of user who put up listing
     */
    private final View.OnClickListener profileButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //OnClick for ProfileButton
            //Send userID as String through Intent to UserProfile Activity
            //Activity should then handle userID to user Info to populate the activity.
        }
    };

    /**
     * OnClick to send notification to seller notifying the interest in the product
     */
    private final View.OnClickListener buyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //OnClick to buy Listing.
            //Send Notification to Seller that user wishes to purchase.
            //Use Notification Channel to establish priority - Oreo and up.
        }
    };
}