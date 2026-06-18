package com.example.luminastay.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luminastay.R;
import com.example.luminastay.database.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RoomDetailActivity extends AppCompatActivity {
    private ImageView ivRoomImage;
    private TextView tvRoomName, tvRoomType, tvRoomPrice, tvDescription, tvFacilities;
    private TextView tvRating, tvReviewCount;
    private TextInputEditText etCheckIn, etCheckOut;
    private MaterialButton btnBook, btnWriteReview;
    private LinearLayout ratingSection;
    private DBHelper dbHelper;

    private int roomId;
    private String roomName;
    private double roomPrice;
    private Calendar checkInCalendar, checkOutCalendar;
    private SimpleDateFormat dateFormat;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        ivRoomImage = findViewById(R.id.ivRoomImage);
        tvRoomName = findViewById(R.id.tvRoomName);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvRoomPrice = findViewById(R.id.tvRoomPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvFacilities = findViewById(R.id.tvFacilities);
        etCheckIn = findViewById(R.id.etCheckIn);
        etCheckOut = findViewById(R.id.etCheckOut);
        btnBook = findViewById(R.id.btnBook);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        tvRating = findViewById(R.id.tvRating);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        ratingSection = findViewById(R.id.ratingSection);

        dbHelper = new DBHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();

        SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
        userEmail = prefs.getString("userEmail", "");

        roomId = getIntent().getIntExtra("roomId", 0);
        roomName = getIntent().getStringExtra("roomName");
        String roomType = getIntent().getStringExtra("roomType");
        String description = getIntent().getStringExtra("roomDescription");
        roomPrice = getIntent().getDoubleExtra("roomPrice", 0.0);
        int imageResource = getIntent().getIntExtra("roomImage", android.R.drawable.ic_menu_gallery);
        String facilities = getIntent().getStringExtra("roomFacilities");

        if (roomId <= 0 || roomName == null || roomName.isEmpty() || roomPrice <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "Data kamar tidak valid", Snackbar.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvRoomName.setText(roomName);
        tvRoomType.setText(roomType);
        tvRoomPrice.setText("Rp " + String.format("%,.0f", roomPrice));
        tvDescription.setText(description);
        tvFacilities.setText(facilities);
        ivRoomImage.setImageResource(imageResource);

        loadRating();

        if (btnWriteReview != null) {
            boolean hasCompletedBooking = checkUserHasCompletedBooking();
            if (hasCompletedBooking) {
                btnWriteReview.setVisibility(View.VISIBLE);
                btnWriteReview.setOnClickListener(v -> showReviewDialog());
            } else {
                btnWriteReview.setVisibility(View.GONE);
            }
        }

        MaterialButton btnOpenMaps = findViewById(R.id.btnOpenMaps);
        if (btnOpenMaps != null) {
            btnOpenMaps.setOnClickListener(v -> openGoogleMaps());
        }

        setupDatePickers();

        btnBook.setOnClickListener(v -> bookRoom());
    }

    private void setupDatePickers() {
        etCheckIn.setOnClickListener(v -> showDatePicker(true));
        etCheckOut.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(final boolean isCheckIn) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);

                    if (isCheckIn) {
                        checkInCalendar = selectedCalendar;
                        etCheckIn.setText(dateFormat.format(selectedCalendar.getTime()));
                    } else {
                        checkOutCalendar = selectedCalendar;
                        etCheckOut.setText(dateFormat.format(selectedCalendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void bookRoom() {
        String checkIn = etCheckIn.getText().toString().trim();
        String checkOut = etCheckOut.getText().toString().trim();

        if (checkIn.isEmpty() || checkOut.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Mohon pilih tanggal check-in dan check-out",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (checkInCalendar.getTimeInMillis() >= checkOutCalendar.getTimeInMillis()) {
            Snackbar.make(findViewById(android.R.id.content), "Tanggal check-out harus setelah check-in",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        long diffInMillis = checkOutCalendar.getTimeInMillis() - checkInCalendar.getTimeInMillis();
        long nights = Math.max(1, TimeUnit.MILLISECONDS.toDays(diffInMillis));

        double totalPrice = nights * roomPrice;

        if (roomPrice <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "Harga kamar tidak valid. Silakan coba lagi.",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        showPromoCodeDialog(checkIn, checkOut, totalPrice);
    }

    private void showPromoCodeDialog(String checkIn, String checkOut, double totalPrice) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_promo_code, null);
        TextInputEditText etPromoCode = dialogView.findViewById(R.id.etPromoCode);
        TextView tvOriginalPrice = dialogView.findViewById(R.id.tvOriginalPrice);
        TextView tvDiscount = dialogView.findViewById(R.id.tvDiscount);
        TextView tvFinalPrice = dialogView.findViewById(R.id.tvFinalPrice);
        LinearLayout promoSection = dialogView.findViewById(R.id.promoSection);

        tvOriginalPrice.setText("Rp " + String.format("%,.0f", totalPrice));
        promoSection.setVisibility(View.GONE);

        final double[] originalTotalPrice = {totalPrice};

        etPromoCode.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String promoCode = s.toString().trim();
                double currentTotal = originalTotalPrice[0];

                if (promoCode.equalsIgnoreCase("LUMINA10")) {
                    double discount = currentTotal * 0.10;
                    double finalPrice = currentTotal - discount;
                    tvDiscount.setText("- Rp " + String.format("%,.0f", discount));
                    tvFinalPrice.setText("Rp " + String.format("%,.0f", finalPrice));
                    promoSection.setVisibility(View.VISIBLE);
                } else {
                    promoSection.setVisibility(View.GONE);
                }
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi Booking")
                .setView(dialogView)
                .setPositiveButton("Bayar", null)
                .setNegativeButton("Batal", null);

        AlertDialog bookingDialog = dialogBuilder.create();
        bookingDialog.show();

        bookingDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String promoCode = etPromoCode.getText().toString().trim();
            double discount = 0;
            double finalPrice = originalTotalPrice[0];

            if (promoCode.equalsIgnoreCase("LUMINA10")) {
                discount = originalTotalPrice[0] * 0.10;
                finalPrice = originalTotalPrice[0] - discount;
            }

            boolean isBooked = (discount > 0)
                    ? dbHelper.addBookingWithPromo(userEmail, roomName, checkIn, checkOut, finalPrice, promoCode, discount)
                    : dbHelper.addBooking(userEmail, roomName, checkIn, checkOut, finalPrice);

            if (isBooked) {
                bookingDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.booking_success), Snackbar.LENGTH_SHORT).show();
                showReviewDialogAfterBooking();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Booking gagal. Silakan coba lagi.",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRating() {
        double avgRating = dbHelper.getRoomAverageRating(roomId);
        int reviewCount = dbHelper.getRoomReviewCount(roomId);

        if (avgRating > 0) {
            tvRating.setText(String.format("%.1f", avgRating));
            tvReviewCount.setText("(" + reviewCount + " ulasan)");
            ratingSection.setVisibility(View.VISIBLE);
        } else {
            tvRating.setText("0.0");
            tvReviewCount.setText("(Belum ada ulasan)");
        }
    }

    private void showReviewDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_review, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputEditText etComment = dialogView.findViewById(R.id.etComment);

        ratingBar.setIsIndicator(false);
        ratingBar.setStepSize(0.5f);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("Tulis Ulasan")
                .setView(dialogView)
                .setPositiveButton("Kirim", null)
                .setNegativeButton("Batal", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            double rating = ratingBar.getRating();
            String comment = etComment.getText().toString().trim();

            if (rating == 0) {
                Snackbar.make(dialogView, "Mohon beri rating", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Snackbar.make(dialogView, "Mohon isi komentar", Snackbar.LENGTH_SHORT).show();
                return;
            }

            boolean isAdded = dbHelper.addReview(userEmail, roomId, rating, comment);
            if (isAdded) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.review_submitted), Snackbar.LENGTH_SHORT).show();
                loadRating();
                dialog.dismiss();
            } else {
                Snackbar.make(dialogView, "Gagal mengirim ulasan", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showReviewDialogAfterBooking() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_review, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputEditText etComment = dialogView.findViewById(R.id.etComment);

        ratingBar.setIsIndicator(false);
        ratingBar.setStepSize(0.5f);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("Terima kasih! Tulis Ulasan Anda")
                .setMessage("Bagaimana pengalaman Anda menginap di " + roomName + "?")
                .setView(dialogView)
                .setPositiveButton("Kirim", null)
                .setNegativeButton("Lewati", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            double rating = ratingBar.getRating();
            String comment = etComment.getText().toString().trim();

            if (rating == 0) {
                Snackbar.make(dialogView, "Mohon beri rating", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Snackbar.make(dialogView, "Mohon isi komentar", Snackbar.LENGTH_SHORT).show();
                return;
            }

            boolean isAdded = dbHelper.addReview(userEmail, roomId, rating, comment);
            if (isAdded) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.review_submitted), Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            } else {
                Snackbar.make(dialogView, "Gagal mengirim ulasan", Snackbar.LENGTH_SHORT).show();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }

    private boolean checkUserHasCompletedBooking() {
        List<com.example.luminastay.models.Booking> bookings = dbHelper.getUserBookings(userEmail);
        for (com.example.luminastay.models.Booking booking : bookings) {
            if (booking.getRoomName().equals(roomName) &&
                    (booking.getStatus().equals("Terkonfirmasi") || booking.getStatus().equals("Selesai"))) {
                return true;
            }
        }
        return false;
    }

    private void openGoogleMaps() {
        double latitude = -6.2088;
        double longitude = 106.8456;
        String hotelName = "Hotel Lumina Stay";

        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode(hotelName));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
            startActivity(new Intent(Intent.ACTION_VIEW, webUri));
        }
    }
}
