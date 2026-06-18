package com.example.luminastay.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.activities.RoomDetailActivity;
import com.example.luminastay.adapters.RoomAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Room;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoomListFragment extends Fragment {
    private RecyclerView rvRooms;
    private TextInputEditText etSearch;
    private MaterialButton btnFilter, btnSort;
    private LinearLayout emptyState;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private List<Room> filteredRoomList;
    private DBHelper dbHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvRooms = view.findViewById(R.id.rvRooms);
        etSearch = view.findViewById(R.id.etSearch);
        btnFilter = view.findViewById(R.id.btnFilter);
        btnSort = view.findViewById(R.id.btnSort);
        emptyState = view.findViewById(R.id.emptyState);

        dbHelper = new DBHelper(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("LuminaStayPrefs", 0);
        userEmail = prefs.getString("userEmail", "");

        // Setup RecyclerView
        rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        roomList = getRoomData();
        filteredRoomList = new ArrayList<>(roomList);
        loadRatings();
        roomAdapter = new RoomAdapter(getContext(), filteredRoomList, userEmail, dbHelper);
        rvRooms.setAdapter(roomAdapter);

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRooms(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter button
        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Sort button
        btnSort.setOnClickListener(v -> showSortDialog());

        updateEmptyState();

        return view;
    }

    private List<Room> getRoomData() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(new Room(1, "Standard Room", "Standard",
                "Kamar nyaman dengan fasilitas standar yang cocok untuk perjalanan bisnis maupun liburan.",
                350000, R.drawable.standard_room,
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 32 inch\n✓ Kamar Mandi Pribadi\n✓ Meja Kerja"));

        rooms.add(new Room(2, "Deluxe Ocean View", "Deluxe",
                "Kamar luas dengan pemandangan laut yang menakjubkan. Dilengkapi dengan fasilitas premium.",
                750000, R.drawable.delux_ocean_view,
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 42 inch\n✓ Sarapan Gratis\n✓ Kamar Mandi Pribadi\n✓ Balkon\n✓ Mini Bar"));

        rooms.add(new Room(3, "Suite Presidential", "Suite",
                "Suite mewah dengan ruang tamu terpisah dan fasilitas kelas dunia.",
                1500000, R.drawable.suite_presidential,
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 55 inch\n✓ Sarapan Gratis\n✓ 2 Kamar Mandi\n✓ Jacuzzi\n✓ Living Room\n✓ Mini Bar\n✓ Butler Service"));

        rooms.add(new Room(4, "Family Room", "Deluxe",
                "Kamar keluarga yang luas dengan 2 tempat tidur dan ruang bermain.",
                950000, R.drawable.family_room,
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 42 inch\n✓ Sarapan Gratis\n✓ 2 Tempat Tidur\n✓ Play Area\n✓ Kamar Mandi Luas"));

        rooms.add(new Room(5, "Romantic Suite", "Suite",
                "Suite romantis dengan dekorasi elegan dan pemandangan kota.",
                1200000, R.drawable.romantic_suite,
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 50 inch\n✓ Sarapan di Kamar\n✓ Bathtub\n✓ Balkon Pribadi\n✓ Wine Complimentary\n✓ Dekorasi Romantis"));

        return rooms;
    }

    private void loadRatings() {
        for (Room room : roomList) {
            double avgRating = dbHelper.getRoomAverageRating(room.getId());
            int reviewCount = dbHelper.getRoomReviewCount(room.getId());
            room.setRating(avgRating);
            room.setReviewCount(reviewCount);
        }
    }

    private void filterRooms(String query) {
        filteredRoomList.clear();
        if (query.isEmpty()) {
            filteredRoomList.addAll(roomList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Room room : roomList) {
                if (room.getName().toLowerCase().contains(lowerQuery) ||
                    room.getType().toLowerCase().contains(lowerQuery) ||
                    String.valueOf(room.getPricePerNight()).contains(query)) {
                    filteredRoomList.add(room);
                }
            }
        }
        roomAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void showFilterDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        CheckBox cbWifi = dialogView.findViewById(R.id.cbWifi);
        CheckBox cbAC = dialogView.findViewById(R.id.cbAC);
        CheckBox cbPool = dialogView.findViewById(R.id.cbPool);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        Spinner spinnerPriceRange = dialogView.findViewById(R.id.spinnerPriceRange);

        // Setup type spinner
        String[] types = {"Semua", "Standard", "Deluxe", "Suite"};
        android.widget.ArrayAdapter<String> typeAdapter = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // Setup price range spinner
        String[] priceRanges = {"Semua", "Dibawah 500rb", "500rb - 1jt", "Diatas 1jt"};
        android.widget.ArrayAdapter<String> priceAdapter = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, priceRanges);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRange.setAdapter(priceAdapter);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Filter Hotel")
                .setView(dialogView)
                .setPositiveButton("Terapkan", (dialog, which) -> {
                    applyFilters(cbWifi.isChecked(), cbAC.isChecked(), cbPool.isChecked(), 
                            spinnerType.getSelectedItemPosition(), spinnerPriceRange.getSelectedItemPosition());
                })
                .setNegativeButton("Reset", (dialog, which) -> {
                    filteredRoomList.clear();
                    filteredRoomList.addAll(roomList);
                    roomAdapter.notifyDataSetChanged();
                    updateEmptyState();
                })
                .show();
    }

    private void applyFilters(boolean wifi, boolean ac, boolean pool, int typeIndex, int priceIndex) {
        filteredRoomList.clear();
        
        for (Room room : roomList) {
            boolean matches = true;
            
            // Filter by type
            if (typeIndex > 0) {
                String[] types = {"Semua", "Standard", "Deluxe", "Suite"};
                if (!room.getType().equals(types[typeIndex])) {
                    matches = false;
                }
            }
            
            // Filter by facilities
            String facilities = room.getFacilities().toLowerCase();
            if (wifi && !facilities.contains("wifi")) {
                matches = false;
            }
            if (ac && !facilities.contains("ac")) {
                matches = false;
            }
            if (pool && !facilities.contains("kolam") && !facilities.contains("renang")) {
                matches = false;
            }
            
            // Filter by price range
            if (priceIndex > 0) {
                double price = room.getPricePerNight();
                if (priceIndex == 1 && price >= 500000) { // Dibawah 500rb
                    matches = false;
                } else if (priceIndex == 2 && (price < 500000 || price > 1000000)) { // 500rb - 1jt
                    matches = false;
                } else if (priceIndex == 3 && price <= 1000000) { // Diatas 1jt
                    matches = false;
                }
            }
            
            if (matches) {
                filteredRoomList.add(room);
            }
        }
        
        roomAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void showSortDialog() {
        String[] sortOptions = {
                getString(R.string.sort_price_asc),
                getString(R.string.sort_price_desc),
                getString(R.string.sort_rating)
        };

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Urutkan")
                .setItems(sortOptions, (dialog, which) -> {
                    if (which == 0) {
                        Collections.sort(filteredRoomList, Comparator.comparingDouble(Room::getPricePerNight));
                    } else if (which == 1) {
                        Collections.sort(filteredRoomList, (r1, r2) -> Double.compare(r2.getPricePerNight(), r1.getPricePerNight()));
                    } else if (which == 2) {
                        Collections.sort(filteredRoomList, (r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
                    }
                    roomAdapter.notifyDataSetChanged();
                })
                .show();
    }

    private void updateEmptyState() {
        if (filteredRoomList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvRooms.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvRooms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRatings();
        if (roomAdapter != null) {
            roomAdapter.notifyDataSetChanged();
        }
    }
}

