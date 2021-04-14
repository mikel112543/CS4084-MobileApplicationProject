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

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton newListingButton;

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