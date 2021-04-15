package com.example.cs4084_project_farm_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton newListingButton;
    private MenuItem profileButton;
    private boolean showSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        newListingButton = (FloatingActionButton) findViewById(R.id.new_listing_button);
        newListingButton.setOnClickListener(floatingButtonOnClickListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        collapsingToolbarLayout.setTitle("Home");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        return true;
    }

    /**
     * @param item OnClick Listener for Menu Item based off the Menu Item ID
     * @return UserID is passed into intent to ProfilePage Activity
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_btn) {
            /*Intent intent = new Intent(MainActivity.this, ProfilePage.class);
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            intent.putExtra("UserID", userID);
            startActivity(intent);*/
        }
        return super.onOptionsItemSelected(item);
    }

    private final MenuItem.OnMenuItemClickListener profileBtnListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
    };

    private final View.OnClickListener floatingButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, NewListingActivity.class));

        }
    };

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
                            showSearch = false;
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            collapsingToolbarLayout.setTitle("Search");
                            newListingButton.setVisibility(View.GONE);
                            showSearch = true;
                            break;
                        case R.id.nav_notification:
                            selectedFragment = new NotificationFragment();
                            collapsingToolbarLayout.setTitle("Notifications");
                            newListingButton.setVisibility(View.GONE);
                            showSearch = false;
                            break;
                        case R.id.nav_messages:
                            selectedFragment = new MessagesFragment();
                            collapsingToolbarLayout.setTitle("Messages");
                            newListingButton.setVisibility(View.GONE);
                            showSearch = false;
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}