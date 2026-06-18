package com.example.luminastay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.luminastay.models.Booking;
import com.example.luminastay.models.TransaksiLayanan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LuminaStay.db";
    private static final int DATABASE_VERSION = 3;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";

    // Bookings Table
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COL_BOOKING_ID = "id";
    private static final String COL_BOOKING_USER_EMAIL = "user_email";
    private static final String COL_BOOKING_ROOM_NAME = "room_name";
    private static final String COL_BOOKING_CHECK_IN = "check_in_date";
    private static final String COL_BOOKING_CHECK_OUT = "check_out_date";
    private static final String COL_BOOKING_TOTAL_PRICE = "total_price";
    private static final String COL_BOOKING_STATUS = "status";
    private static final String COL_BOOKING_DATE = "booking_date";

    // Favorites Table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COL_FAVORITE_ID = "id";
    private static final String COL_FAVORITE_USER_EMAIL = "user_email";
    private static final String COL_FAVORITE_ROOM_ID = "room_id";

    // Reviews Table
    private static final String TABLE_REVIEWS = "reviews";
    private static final String COL_REVIEW_ID = "id";
    private static final String COL_REVIEW_USER_EMAIL = "user_email";
    private static final String COL_REVIEW_ROOM_ID = "room_id";
    private static final String COL_REVIEW_RATING = "rating";
    private static final String COL_REVIEW_COMMENT = "comment";
    private static final String COL_REVIEW_DATE = "review_date";

    // Transaksi Layanan Table
    private static final String TABLE_TRANSAKSI_LAYANAN = "transaksi_layanan";
    private static final String COL_TRANSAKSI_ID = "id";
    private static final String COL_TRANSAKSI_USER_EMAIL = "user_email";
    private static final String COL_TRANSAKSI_TANGGAL = "tanggal";
    private static final String COL_TRANSAKSI_TOTAL_HARGA = "total_harga";
    private static final String COL_TRANSAKSI_STATUS = "status";
    private static final String COL_TRANSAKSI_ITEMS = "items"; // JSON string

    // Booking with Promo Code
    private static final String COL_BOOKING_PROMO_CODE = "promo_code";
    private static final String COL_BOOKING_DISCOUNT = "discount";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Bookings Table
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOKING_USER_EMAIL + " TEXT, " +
                COL_BOOKING_ROOM_NAME + " TEXT, " +
                COL_BOOKING_CHECK_IN + " TEXT, " +
                COL_BOOKING_CHECK_OUT + " TEXT, " +
                COL_BOOKING_TOTAL_PRICE + " REAL, " +
                COL_BOOKING_STATUS + " TEXT, " +
                COL_BOOKING_DATE + " TEXT, " +
                COL_BOOKING_PROMO_CODE + " TEXT, " +
                COL_BOOKING_DISCOUNT + " REAL DEFAULT 0)";
        db.execSQL(createBookingsTable);

        // Create Favorites Table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FAVORITE_USER_EMAIL + " TEXT, " +
                COL_FAVORITE_ROOM_ID + " INTEGER, " +
                "UNIQUE(" + COL_FAVORITE_USER_EMAIL + ", " + COL_FAVORITE_ROOM_ID + "))";
        db.execSQL(createFavoritesTable);

        // Create Reviews Table
        String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REVIEW_USER_EMAIL + " TEXT, " +
                COL_REVIEW_ROOM_ID + " INTEGER, " +
                COL_REVIEW_RATING + " REAL, " +
                COL_REVIEW_COMMENT + " TEXT, " +
                COL_REVIEW_DATE + " TEXT)";
        db.execSQL(createReviewsTable);

        // Create Transaksi Layanan Table
        String createTransaksiTable = "CREATE TABLE " + TABLE_TRANSAKSI_LAYANAN + " (" +
                COL_TRANSAKSI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRANSAKSI_USER_EMAIL + " TEXT, " +
                COL_TRANSAKSI_TANGGAL + " TEXT, " +
                COL_TRANSAKSI_TOTAL_HARGA + " REAL, " +
                COL_TRANSAKSI_STATUS + " TEXT, " +
                COL_TRANSAKSI_ITEMS + " TEXT)";
        db.execSQL(createTransaksiTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create new tables for version 2
            String createFavoritesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (" +
                    COL_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_FAVORITE_USER_EMAIL + " TEXT, " +
                    COL_FAVORITE_ROOM_ID + " INTEGER, " +
                    "UNIQUE(" + COL_FAVORITE_USER_EMAIL + ", " + COL_FAVORITE_ROOM_ID + "))";
            db.execSQL(createFavoritesTable);

            String createReviewsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_REVIEWS + " (" +
                    COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_REVIEW_USER_EMAIL + " TEXT, " +
                    COL_REVIEW_ROOM_ID + " INTEGER, " +
                    COL_REVIEW_RATING + " REAL, " +
                    COL_REVIEW_COMMENT + " TEXT, " +
                    COL_REVIEW_DATE + " TEXT)";
            db.execSQL(createReviewsTable);
        }
        
        if (oldVersion < 3) {
            // Create Transaksi Layanan Table for version 3
            String createTransaksiTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSAKSI_LAYANAN + " (" +
                    COL_TRANSAKSI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TRANSAKSI_USER_EMAIL + " TEXT, " +
                    COL_TRANSAKSI_TANGGAL + " TEXT, " +
                    COL_TRANSAKSI_TOTAL_HARGA + " REAL, " +
                    COL_TRANSAKSI_STATUS + " TEXT, " +
                    COL_TRANSAKSI_ITEMS + " TEXT)";
            db.execSQL(createTransaksiTable);
            
            // Add promo code columns to bookings table
            try {
                db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN " + COL_BOOKING_PROMO_CODE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN " + COL_BOOKING_DISCOUNT + " REAL DEFAULT 0");
            } catch (Exception e) {
                // Column might already exist
            }
        }
    }

    // User Operations
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_NAME},
                COL_USER_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public boolean updateUserProfile(String email, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, newName);

        int result = db.update(TABLE_USERS, values,
                COL_USER_EMAIL + "=?", new String[]{email});
        return result > 0;
    }

    // Booking Operations
    public boolean addBooking(String userEmail, String roomName, String checkIn,
                              String checkOut, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        values.put(COL_BOOKING_USER_EMAIL, userEmail);
        values.put(COL_BOOKING_ROOM_NAME, roomName);
        values.put(COL_BOOKING_CHECK_IN, checkIn);
        values.put(COL_BOOKING_CHECK_OUT, checkOut);
        values.put(COL_BOOKING_TOTAL_PRICE, totalPrice);
        values.put(COL_BOOKING_STATUS, "Terkonfirmasi");
        values.put(COL_BOOKING_DATE, currentDate);
        // Set promo code and discount to null/0 for regular bookings
        values.put(COL_BOOKING_PROMO_CODE, "");
        values.put(COL_BOOKING_DISCOUNT, 0.0);

        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }

    public List<Booking> getUserBookings(String userEmail) {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKINGS,
                null,
                COL_BOOKING_USER_EMAIL + "=?",
                new String[]{userEmail},
                null, null,
                COL_BOOKING_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_ID)));
                booking.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_USER_EMAIL)));
                booking.setRoomName(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_ROOM_NAME)));
                booking.setCheckInDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_CHECK_IN)));
                booking.setCheckOutDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_CHECK_OUT)));
                booking.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOKING_TOTAL_PRICE)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_STATUS)));
                booking.setBookingDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_DATE)));

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookingList;
    }

    // Favorite Operations
    public boolean addFavorite(String userEmail, int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FAVORITE_USER_EMAIL, userEmail);
        values.put(COL_FAVORITE_ROOM_ID, roomId);

        long result = db.insertWithOnConflict(TABLE_FAVORITES, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    public boolean removeFavorite(String userEmail, int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FAVORITES,
                COL_FAVORITE_USER_EMAIL + "=? AND " + COL_FAVORITE_ROOM_ID + "=?",
                new String[]{userEmail, String.valueOf(roomId)});
        return result > 0;
    }

    public boolean isFavorite(String userEmail, int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{COL_FAVORITE_ID},
                COL_FAVORITE_USER_EMAIL + "=? AND " + COL_FAVORITE_ROOM_ID + "=?",
                new String[]{userEmail, String.valueOf(roomId)},
                null, null, null);

        boolean isFav = cursor.getCount() > 0;
        cursor.close();
        return isFav;
    }

    public List<Integer> getUserFavorites(String userEmail) {
        List<Integer> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{COL_FAVORITE_ROOM_ID},
                COL_FAVORITE_USER_EMAIL + "=?",
                new String[]{userEmail},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                favoriteList.add(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAVORITE_ROOM_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteList;
    }

    // Review Operations
    public boolean addReview(String userEmail, int roomId, double rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        values.put(COL_REVIEW_USER_EMAIL, userEmail);
        values.put(COL_REVIEW_ROOM_ID, roomId);
        values.put(COL_REVIEW_RATING, rating);
        values.put(COL_REVIEW_COMMENT, comment);
        values.put(COL_REVIEW_DATE, currentDate);

        long result = db.insert(TABLE_REVIEWS, null, values);
        return result != -1;
    }

    public double getRoomAverageRating(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COL_REVIEW_RATING + ") FROM " +
                TABLE_REVIEWS + " WHERE " + COL_REVIEW_ROOM_ID + "=?", new String[]{String.valueOf(roomId)});

        double avgRating = 0.0;
        if (cursor.moveToFirst()) {
            avgRating = cursor.getDouble(0);
        }
        cursor.close();
        return avgRating;
    }

    public int getRoomReviewCount(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REVIEWS +
                " WHERE " + COL_REVIEW_ROOM_ID + "=?", new String[]{String.valueOf(roomId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<Review> getRoomReviews(int roomId) {
        List<Review> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REVIEWS,
                null,
                COL_REVIEW_ROOM_ID + "=?",
                new String[]{String.valueOf(roomId)},
                null, null,
                COL_REVIEW_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_REVIEW_ID)));
                review.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_REVIEW_USER_EMAIL)));
                review.setRoomId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_REVIEW_ROOM_ID)));
                review.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_REVIEW_RATING)));
                review.setComment(cursor.getString(cursor.getColumnIndexOrThrow(COL_REVIEW_COMMENT)));
                review.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_REVIEW_DATE)));
                reviewList.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviewList;
    }

    // Review model class
    public static class Review {
        private int id;
        private String userEmail;
        private int roomId;
        private double rating;
        private String comment;
        private String date;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
        public int getRoomId() { return roomId; }
        public void setRoomId(int roomId) { this.roomId = roomId; }
        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }

    // Transaksi Layanan Operations
    public boolean addTransaksiLayanan(String userEmail, double totalHarga, String itemsJson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        values.put(COL_TRANSAKSI_USER_EMAIL, userEmail);
        values.put(COL_TRANSAKSI_TANGGAL, currentDate);
        values.put(COL_TRANSAKSI_TOTAL_HARGA, totalHarga);
        values.put(COL_TRANSAKSI_STATUS, "Selesai");
        values.put(COL_TRANSAKSI_ITEMS, itemsJson);

        long result = db.insert(TABLE_TRANSAKSI_LAYANAN, null, values);
        return result != -1;
    }

    public List<TransaksiLayanan> getUserTransaksiLayanan(String userEmail) {
        List<TransaksiLayanan> transaksiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSAKSI_LAYANAN,
                null,
                COL_TRANSAKSI_USER_EMAIL + "=?",
                new String[]{userEmail},
                null, null,
                COL_TRANSAKSI_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                TransaksiLayanan transaksi = new TransaksiLayanan();
                transaksi.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRANSAKSI_ID)));
                transaksi.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSAKSI_USER_EMAIL)));
                transaksi.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSAKSI_TANGGAL)));
                transaksi.setTotalHarga(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TRANSAKSI_TOTAL_HARGA)));
                transaksi.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSAKSI_STATUS)));
                transaksiList.add(transaksi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transaksiList;
    }

    // Booking with Promo Code
    public boolean addBookingWithPromo(String userEmail, String roomName, String checkIn,
                                      String checkOut, double totalPrice, String promoCode, double discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        values.put(COL_BOOKING_USER_EMAIL, userEmail);
        values.put(COL_BOOKING_ROOM_NAME, roomName);
        values.put(COL_BOOKING_CHECK_IN, checkIn);
        values.put(COL_BOOKING_CHECK_OUT, checkOut);
        values.put(COL_BOOKING_TOTAL_PRICE, totalPrice);
        values.put(COL_BOOKING_STATUS, "Terkonfirmasi");
        values.put(COL_BOOKING_DATE, currentDate);
        values.put(COL_BOOKING_PROMO_CODE, promoCode);
        values.put(COL_BOOKING_DISCOUNT, discount);

        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }
}