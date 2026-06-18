package com.example.luminastay.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.luminastay.R;
import com.example.luminastay.activities.LayananActivity;
import com.example.luminastay.activities.LoginActivity;
import com.example.luminastay.activities.RiwayatLayananActivity;
import com.example.luminastay.database.DBHelper;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {
    private TextView tvUserName, tvUserEmail;
    private MaterialCardView cardEditProfile;
    private MaterialCardView cardLayanan;
    private MaterialCardView cardRiwayatLayanan;
    private MaterialCardView cardLogout;
    private SwitchMaterial switchDarkMode;
    private DBHelper dbHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        cardEditProfile = view.findViewById(R.id.cardEditProfile);
        cardLayanan = view.findViewById(R.id.cardLayanan);
        cardRiwayatLayanan = view.findViewById(R.id.cardRiwayatLayanan);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        cardLogout = view.findViewById(R.id.cardLogout);

        dbHelper = new DBHelper(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("LuminaStayPrefs", 0);
        userEmail = prefs.getString("userEmail", "");

        // Load user info
        String userName = dbHelper.getUserName(userEmail);
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        // Load dark mode preference
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        switchDarkMode.setChecked(isDarkMode);

        // Edit Profile click
        cardEditProfile.setOnClickListener(v -> showEditProfileDialog());

        // Layanan click
        if (cardLayanan != null) {
            cardLayanan.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LayananActivity.class);
                startActivity(intent);
            });
        }

        // Riwayat Layanan click
        if (cardRiwayatLayanan != null) {
            cardRiwayatLayanan.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), RiwayatLayananActivity.class);
                startActivity(intent);
            });
        }

        // Dark Mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Logout click
        cardLogout = view.findViewById(R.id.cardLogout);
        if (cardLogout != null) {
            cardLogout.setOnClickListener(v -> showLogoutDialog());
        }

        return view;
    }

    private void showEditProfileDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        TextInputEditText etNewName = dialogView.findViewById(R.id.etNewName);
        etNewName.setText(tvUserName.getText());

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Edit Profil")
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String newName = etNewName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        boolean isUpdated = dbHelper.updateUserProfile(userEmail, newName);
                        if (isUpdated) {
                            tvUserName.setText(newName);
                            Snackbar.make(getView(), "Profil berhasil diperbarui", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(getView(), "Gagal memperbarui profil", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    SharedPreferences prefs = getActivity().getSharedPreferences("LuminaStayPrefs", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}

