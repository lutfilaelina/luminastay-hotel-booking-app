package com.example.luminastay.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.adapters.BookingAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Booking;

import java.util.List;

public class BookingHistoryFragment extends Fragment {
    private RecyclerView rvBookings;
    private LinearLayout emptyState;
    private BookingAdapter bookingAdapter;
    private DBHelper dbHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        rvBookings = view.findViewById(R.id.rvBookings);
        emptyState = view.findViewById(R.id.emptyState);

        dbHelper = new DBHelper(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("LuminaStayPrefs", 0);
        userEmail = prefs.getString("userEmail", "");

        // Setup RecyclerView
        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load bookings
        loadBookings();

        return view;
    }

    private void loadBookings() {
        List<Booking> bookingList = dbHelper.getUserBookings(userEmail);

        if (bookingList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBookings.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBookings.setVisibility(View.VISIBLE);
            bookingAdapter = new BookingAdapter(getContext(), bookingList);
            rvBookings.setAdapter(bookingAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }
}

