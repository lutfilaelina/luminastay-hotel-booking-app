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
import com.example.luminastay.adapters.RoomAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Room;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView rvFavorites;
    private LinearLayout emptyState;
    private RoomAdapter roomAdapter;
    private List<Room> favoriteRoomList;
    private DBHelper dbHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        emptyState = view.findViewById(R.id.emptyState);

        dbHelper = new DBHelper(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("LuminaStayPrefs", 0);
        userEmail = prefs.getString("userEmail", "");

        // Setup RecyclerView
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load favorites
        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        List<Integer> favoriteIds = dbHelper.getUserFavorites(userEmail);
        List<Room> allRooms = getAllRooms();
        favoriteRoomList = new ArrayList<>();

        for (Room room : allRooms) {
            if (favoriteIds.contains(room.getId())) {
                double avgRating = dbHelper.getRoomAverageRating(room.getId());
                int reviewCount = dbHelper.getRoomReviewCount(room.getId());
                room.setRating(avgRating);
                room.setReviewCount(reviewCount);
                favoriteRoomList.add(room);
            }
        }

        if (favoriteRoomList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvFavorites.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.VISIBLE);
            roomAdapter = new RoomAdapter(getContext(), favoriteRoomList, userEmail, dbHelper);
            rvFavorites.setAdapter(roomAdapter);
        }
    }

    private List<Room> getAllRooms() {
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

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}

