package com.example.luminastay.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.luminastay.R;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.luminastay.fragments.BookingHistoryFragment;
import com.example.luminastay.fragments.FavoriteFragment;
import com.example.luminastay.fragments.ProfileFragment;
import com.example.luminastay.fragments.RoomListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(navListener);

        // Apply dark mode preference on startup
        SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Load fragment based on intent or default
        if (savedInstanceState == null) {
            String fragmentName = getIntent().getStringExtra("fragment");
            Fragment defaultFragment = new RoomListFragment();
            
            if (fragmentName != null) {
                if (fragmentName.equals("booking")) {
                    defaultFragment = new BookingHistoryFragment();
                    bottomNavigation.setSelectedItemId(R.id.nav_booking);
                } else if (fragmentName.equals("profile")) {
                    defaultFragment = new ProfileFragment();
                    bottomNavigation.setSelectedItemId(R.id.nav_profile);
                }
            }
            
            loadFragment(defaultFragment);
        }
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        selectedFragment = new RoomListFragment();
                    } else if (itemId == R.id.nav_booking) {
                        selectedFragment = new BookingHistoryFragment();
                    } else if (itemId == R.id.nav_favorite) {
                        selectedFragment = new FavoriteFragment();
                    } else if (itemId == R.id.nav_profile) {
                        selectedFragment = new ProfileFragment();
                    }

                    if (selectedFragment != null) {
                        loadFragment(selectedFragment);
                    }

                    return true;
                }
            };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    @Deprecated
    public void onBackPressed() {
        if (bottomNavigation.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed();
        } else {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }
}

