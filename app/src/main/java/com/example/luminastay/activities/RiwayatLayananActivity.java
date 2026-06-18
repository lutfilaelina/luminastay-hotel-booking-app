package com.example.luminastay.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.adapters.TransaksiLayananAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.TransaksiLayanan;

import java.util.List;

public class RiwayatLayananActivity extends AppCompatActivity {
    private RecyclerView rvTransaksi;
    private LinearLayout emptyState;
    private TransaksiLayananAdapter adapter;
    private DBHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_layanan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Riwayat Layanan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvTransaksi = findViewById(R.id.rvTransaksi);
        emptyState = findViewById(R.id.emptyState);

        dbHelper = new DBHelper(this);
        SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
        userEmail = prefs.getString("userEmail", "");

        // Setup RecyclerView
        rvTransaksi.setLayoutManager(new LinearLayoutManager(this));
        loadTransaksi();
    }

    private void loadTransaksi() {
        List<TransaksiLayanan> transaksiList = dbHelper.getUserTransaksiLayanan(userEmail);

        if (transaksiList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvTransaksi.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvTransaksi.setVisibility(View.VISIBLE);
            adapter = new TransaksiLayananAdapter(this, transaksiList);
            rvTransaksi.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransaksi();
    }
}
