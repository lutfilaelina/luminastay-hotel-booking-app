package com.example.luminastay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.adapters.RoomAdapter;
import com.example.luminastay.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {
    private RecyclerView rvRooms;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private ImageView btnHistory, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // Initialize views
        rvRooms = findViewById(R.id.rvRooms);
        btnHistory = findViewById(R.id.btnHistory);
        btnProfile = findViewById(R.id.btnProfile);

        // Setup RecyclerView
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        roomList = getRoomData();
        roomAdapter = new RoomAdapter(this, roomList);
        rvRooms.setAdapter(roomAdapter);

        // History button click - Navigate to MainActivity with BookingHistoryFragment
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this, MainActivity.class);
                intent.putExtra("fragment", "booking");
                startActivity(intent);
            }
        });

        // Profile button click - Navigate to MainActivity with ProfileFragment
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this, MainActivity.class);
                intent.putExtra("fragment", "profile");
                startActivity(intent);
            }
        });
    }

    private List<Room> getRoomData() {
        List<Room> rooms = new ArrayList<>();

        // PENTING: Ganti R.drawable.room_standard dengan nama file gambar yang Anda upload
        // Contoh: jika file bernama "kamar_standard.jpg", gunakan R.drawable.kamar_standard

        // Room 1: Standard Room
        rooms.add(new Room(
                1,
                "Standard",
                "Standard",
                "Kamar nyaman dengan fasilitas standar yang cocok untuk perjalanan bisnis maupun liburan. Dilengkapi dengan tempat tidur yang nyaman dan area kerja.",
                350000,
                R.drawable.standard_room, // GANTI dengan nama file Anda
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 32 inch\n✓ Kamar Mandi Pribadi\n✓ Meja Kerja"
        ));

        // Room 2: Deluxe Ocean View
        rooms.add(new Room(
                2,
                "Deluxe Ocean View",
                "Deluxe",
                "Kamar luas dengan pemandangan laut yang menakjubkan. Dilengkapi dengan fasilitas premium untuk pengalaman menginap yang tak terlupakan.",
                750000,
                R.drawable.standard_room, // GANTI dengan nama file Anda
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 42 inch\n✓ Sarapan Gratis\n✓ Kamar Mandi Pribadi\n✓ Balkon\n✓ Mini Bar"
        ));

        // Room 3: Suite Presidential
        rooms.add(new Room(
                3,
                "Suite Presidential",
                "Suite",
                "Suite mewah dengan ruang tamu terpisah dan fasilitas kelas dunia. Sempurna untuk pengalaman menginap yang eksklusif dan istimewa.",
                1500000,
                R.drawable.standard_room, // GANTI dengan nama file Anda
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 55 inch\n✓ Sarapan Gratis\n✓ 2 Kamar Mandi\n✓ Jacuzzi\n✓ Living Room\n✓ Mini Bar\n✓ Butler Service"
        ));

        // Room 4: Family Room
        rooms.add(new Room(
                4,
                "Family Room",
                "Deluxe",
                "Kamar keluarga yang luas dengan 2 tempat tidur dan ruang bermain. Ideal untuk liburan bersama keluarga tercinta.",
                950000,
                R.drawable.standard_room, // GANTI dengan nama file Anda
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 42 inch\n✓ Sarapan Gratis\n✓ 2 Tempat Tidur\n✓ Play Area\n✓ Kamar Mandi Luas"
        ));

        // Room 5: Romantic Suite
        rooms.add(new Room(
                5,
                "Romantic Suite",
                "Suite",
                "Suite romantis dengan dekorasi elegan dan pemandangan kota. Sempurna untuk bulan madu atau perayaan anniversary.",
                1200000,
                R.drawable.standard_room, // GANTI dengan nama file Anda
                "✓ WiFi Gratis\n✓ AC\n✓ TV LED 50 inch\n✓ Sarapan di Kamar\n✓ Bathtub\n✓ Balkon Pribadi\n✓ Wine Complimentary\n✓ Dekorasi Romantis"
        ));

        return rooms;
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to splash/login
        finishAffinity();
    }
}