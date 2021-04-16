package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton newListingButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();


        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        newListingButton = findViewById(R.id.new_listing_button);
        newListingButton.setOnClickListener(floatingButtonOnClickListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        collapsingToolbarLayout.setTitle("Home");


    }


    /***
     *
     * @param menu pass in Menu and inflate according to the layout .xml
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        String userID = auth.getCurrentUser().getUid();

        /*
        DocumentReference currentUserRef = db.collection("users").document(userID);
        Task<DocumentSnapshot> userDoc = currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    documentSnapshot.toObject(User.class);
                    User user = documentSnapshot.toObject(User.class);
                    MenuItem ProfilePic = menu.getItem(0);
                    Picasso.get().load(user.getUrl())
                            .fit()
                            .centerCrop()
                            .into((ImageView) ProfilePic);
                }
            }
        }); */


        return true;
    }

    /**
     * OnClick Listener to access Users profile
     */


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_btn) {
        /*Intent intent = new Intent(MainActivity.this, ProfilePage.class);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        intent.putExtra("UserID", userID);
        startActivity(intent);*/
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            String userID = auth.getCurrentUser().getUid();
            intent.putExtra("userID", userID);

            startActivity(new Intent(MainActivity.this, UserProfile.class));
        }
        return super.onOptionsItemSelected(item);
    }

/*
    private final MenuItem.OnMenuItemClickListener profileBtnListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            String userID = auth.getCurrentUser().getUid();
            intent.putExtra("userID", userID);

            startActivity(new Intent(MainActivity.this, UserProfile.class));
            finish();
            return true;
        }
    }; */

    /***
     * OnCLick listener for user to create new listing
     */
    private final View.OnClickListener floatingButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, NewListingActivity.class));

        }
    };

    /***
     * Switch case to change fragments for bottom navigation view
     */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    //Switch case on Nav bar input
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            collapsingToolbarLayout.setTitle("Home");
                            newListingButton.setVisibility(View.VISIBLE);
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            collapsingToolbarLayout.setTitle("Search");
                            newListingButton.setVisibility(View.GONE);
                            break;
                        case R.id.nav_notification:
                            selectedFragment = new NotificationFragment();
                            collapsingToolbarLayout.setTitle("Notifications");
                            newListingButton.setVisibility(View.GONE);
                            break;
                        case R.id.nav_messages:
                            selectedFragment = new MessagesFragment();
                            collapsingToolbarLayout.setTitle("Messages");
                            newListingButton.setVisibility(View.GONE);
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}