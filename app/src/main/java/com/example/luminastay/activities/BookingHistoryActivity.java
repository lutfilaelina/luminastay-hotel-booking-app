package com.example.luminastay.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.adapters.BookingAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Booking;

import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {
    private RecyclerView rvBookings;
    private LinearLayout emptyState;
    private ImageView btnBack;
    private BookingAdapter bookingAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize views
        rvBookings = findViewById(R.id.rvBookings);
        emptyState = findViewById(R.id.emptyState);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        // Setup RecyclerView
        rvBookings.setLayoutManager(new LinearLayoutManager(this));

        // Load bookings
        loadBookings();

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadBookings() {
        SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        List<Booking> bookingList = dbHelper.getUserBookings(userEmail);

        if (bookingList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBookings.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBookings.setVisibility(View.VISIBLE);
            bookingAdapter = new BookingAdapter(this, bookingList);
            rvBookings.setAdapter(bookingAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh bookings when returning to this activity
        loadBookings();
    }
}