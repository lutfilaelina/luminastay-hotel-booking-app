package com.example.luminastay.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.adapters.CartAdapter;
import com.example.luminastay.adapters.LayananAdapter;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Layanan;
import com.example.luminastay.models.TransaksiLayanan;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LayananActivity extends AppCompatActivity implements 
        LayananAdapter.OnAddToCartListener, CartAdapter.OnCartItemChangedListener {

    private RecyclerView rvLayanan, rvCartItems;
    private ExtendedFloatingActionButton fabCart;
    private MaterialButton btnCheckout;
    private TextView tvCartItemCount, tvSubtotal, tvTotalPrice;
    private LayananAdapter layananAdapter;
    private CartAdapter cartAdapter;
    private List<Layanan> layananList;
    private List<TransaksiLayanan.ItemLayanan> cartItems;
    private DBHelper dbHelper;
    private String userEmail;
    private SharedPreferences cartPrefs;
    private BottomSheetDialog bottomSheetCart;
    private View bottomSheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Layanan Tambahan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvLayanan = findViewById(R.id.rvLayanan);
        fabCart = findViewById(R.id.fabCart);

        dbHelper = new DBHelper(this);
        cartPrefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
        userEmail = prefs.getString("userEmail", "");

        if (userEmail.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Silakan login terlebih dahulu", Snackbar.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize cart items
        cartItems = loadCartFromPrefs();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Setup RecyclerView with Grid Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvLayanan.setLayoutManager(gridLayoutManager);
        layananList = getLayananData();
        if (layananList == null || layananList.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Tidak ada layanan tersedia", Snackbar.LENGTH_SHORT).show();
        } else {
            layananAdapter = new LayananAdapter(this, layananList, this);
            rvLayanan.setAdapter(layananAdapter);
        }

        // Setup Bottom Sheet
        setupBottomSheet();
        
        // Setup FAB
        fabCart.setOnClickListener(v -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                showCartBottomSheet();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Keranjang kosong", Snackbar.LENGTH_SHORT).show();
            }
        });

        updateCartUI();
    }

    private List<Layanan> getLayananData() {
        List<Layanan> layanan = new ArrayList<>();

        layanan.add(new Layanan(1, "Nasi Goreng Spesial", "Makanan & Minuman", 45000, "Nasi goreng dengan telur, ayam, dan sayuran", R.drawable.nasi_goreng));
        layanan.add(new Layanan(2, "Mie Ayam Bakso", "Makanan & Minuman", 35000, "Mie ayam dengan bakso dan pangsit", R.drawable.mi_ayam_bakso));
        layanan.add(new Layanan(3, "Es Jeruk Peras", "Makanan & Minuman", 15000, "Es jeruk peras segar", R.drawable.es_jeruk));
        layanan.add(new Layanan(4, "Kopi Hitam", "Makanan & Minuman", 12000, "Kopi hitam pilihan", R.drawable.kopi_hitam));
        layanan.add(new Layanan(5, "Jus Alpukat", "Makanan & Minuman", 20000, "Jus alpukat segar", R.drawable.jus_alpukat));

        layanan.add(new Layanan(6, "Bantal Tambahan", "Tambahan Kamar", 25000, "Bantal tambahan untuk kenyamanan", R.drawable.bantal_tambahan));
        layanan.add(new Layanan(7, "Selimut Tambahan", "Tambahan Kamar", 30000, "Selimut tambahan hangat", R.drawable.selimu_tambahan));
        layanan.add(new Layanan(8, "Handuk Tambahan", "Tambahan Kamar", 20000, "Handuk lembut tambahan", R.drawable.handuk_tambahan));
        layanan.add(new Layanan(9, "Sandal Hotel", "Tambahan Kamar", 15000, "Sandal hotel sekali pakai", R.drawable.sandal_tambahan));
        layanan.add(new Layanan(10, "Air Mineral 1.5L", "Tambahan Kamar", 10000, "Air mineral kemasan 1.5 liter", R.drawable.air_mineral));
        layanan.add(new Layanan(11, "Snack Box", "Tambahan Kamar", 35000, "Snack box berisi berbagai camilan", R.drawable.snaack_box));

        return layanan;
    }

    @Override
    public void onAddToCart(Layanan layanan) {
        if (layanan == null || cartItems == null) {
            return;
        }
        
        boolean found = false;
        for (TransaksiLayanan.ItemLayanan item : cartItems) {
            if (item != null && item.getLayananId() == layanan.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cartItems.add(new TransaksiLayanan.ItemLayanan(
                    layanan.getId(), layanan.getName(), 1, layanan.getPrice()));
        }

        saveCartToPrefs();
        updateCartUI();
        
        // Animate FAB
        animateFAB();
        
        // Show success message
        Snackbar.make(findViewById(android.R.id.content), 
                "✓ " + layanan.getName() + " ditambahkan", Snackbar.LENGTH_SHORT).show();
    }

    private void animateFAB() {
        android.animation.ObjectAnimator scaleX = android.animation.ObjectAnimator.ofFloat(fabCart, "scaleX", 1.2f, 1f);
        android.animation.ObjectAnimator scaleY = android.animation.ObjectAnimator.ofFloat(fabCart, "scaleY", 1.2f, 1f);
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        scaleX.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        scaleX.start();
        scaleY.start();
    }

    private void setupBottomSheet() {
        bottomSheetCart = new BottomSheetDialog(this);
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_cart, null);
        bottomSheetCart.setContentView(bottomSheetView);

        rvCartItems = bottomSheetView.findViewById(R.id.rvCartItems);
        tvCartItemCount = bottomSheetView.findViewById(R.id.tvCartItemCount);
        tvSubtotal = bottomSheetView.findViewById(R.id.tvSubtotal);
        tvTotalPrice = bottomSheetView.findViewById(R.id.tvTotalPrice);
        btnCheckout = bottomSheetView.findViewById(R.id.btnCheckout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        
        btnCheckout.setOnClickListener(v -> {
            bottomSheetCart.dismiss();
            showCheckoutDialog();
        });
    }

    private void showCartBottomSheet() {
        if (cartItems == null || cartItems.isEmpty()) {
            return;
        }

        // Update cart adapter
        cartAdapter = new CartAdapter(this, cartItems, layananList, this);
        rvCartItems.setAdapter(cartAdapter);
        
        // Update totals
        updateCartTotals();
        
        // Show bottom sheet
        bottomSheetCart.show();
    }

    private void updateCartTotals() {
        if (cartItems == null || tvCartItemCount == null || tvSubtotal == null || tvTotalPrice == null) {
            return;
        }
        
        int totalItems = 0;
        double totalPrice = 0;

        for (TransaksiLayanan.ItemLayanan item : cartItems) {
            if (item != null) {
                totalItems += item.getQuantity();
                totalPrice += item.getSubtotal();
            }
        }

        tvCartItemCount.setText(totalItems + " item");
        tvSubtotal.setText("Rp " + String.format("%,.0f", totalPrice));
        tvTotalPrice.setText("Rp " + String.format("%,.0f", totalPrice));
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        if (cartItems != null && position < cartItems.size()) {
            TransaksiLayanan.ItemLayanan item = cartItems.get(position);
            if (item != null) {
                item.setQuantity(newQuantity);
                saveCartToPrefs();
                updateCartTotals();
                updateCartUI();
                if (cartAdapter != null) {
                    cartAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    @Override
    public void onItemRemoved(int position) {
        if (cartItems != null && position < cartItems.size()) {
            cartItems.remove(position);
            saveCartToPrefs();
            updateCartUI();
            updateCartTotals();
            if (cartAdapter != null) {
                cartAdapter.notifyItemRemoved(position);
                cartAdapter.notifyItemRangeChanged(position, cartItems.size());
            }
            
            if (cartItems.isEmpty() && bottomSheetCart.isShowing()) {
                bottomSheetCart.dismiss();
            }
        }
    }

    private void updateCartUI() {
        if (cartItems == null) {
            return;
        }
        
        int totalItems = 0;
        double totalPrice = 0;

        for (TransaksiLayanan.ItemLayanan item : cartItems) {
            if (item != null) {
                totalItems += item.getQuantity();
                totalPrice += item.getSubtotal();
            }
        }

        // Update FAB
        if (totalItems > 0) {
            fabCart.setVisibility(View.VISIBLE);
            fabCart.setText("Keranjang (" + totalItems + ")");
        } else {
            fabCart.setVisibility(View.GONE);
        }
    }

    private void saveCartToPrefs() {
        if (cartPrefs == null || cartItems == null) {
            return;
        }
        
        JSONArray jsonArray = new JSONArray();
        for (TransaksiLayanan.ItemLayanan item : cartItems) {
            if (item != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("layananId", item.getLayananId());
                    jsonObject.put("layananName", item.getLayananName());
                    jsonObject.put("quantity", item.getQuantity());
                    jsonObject.put("price", item.getPrice());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        cartPrefs.edit().putString("cartItems", jsonArray.toString()).apply();
    }

    private List<TransaksiLayanan.ItemLayanan> loadCartFromPrefs() {
        List<TransaksiLayanan.ItemLayanan> items = new ArrayList<>();
        if (cartPrefs == null) {
            return items;
        }
        
        String cartJson = cartPrefs.getString("cartItems", "[]");
        try {
            JSONArray jsonArray = new JSONArray(cartJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                items.add(new TransaksiLayanan.ItemLayanan(
                        jsonObject.getInt("layananId"),
                        jsonObject.getString("layananName"),
                        jsonObject.getInt("quantity"),
                        jsonObject.getDouble("price")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }


    private void showCheckoutDialog() {
        if (cartItems == null || cartItems.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Keranjang kosong", Snackbar.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        StringBuilder itemsJson = new StringBuilder("[");
        int validItems = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            TransaksiLayanan.ItemLayanan item = cartItems.get(i);
            if (item != null) {
                total += item.getSubtotal();
                if (validItems > 0) itemsJson.append(",");
                itemsJson.append("{\"id\":").append(item.getLayananId())
                        .append(",\"name\":\"").append(item.getLayananName())
                        .append("\",\"qty\":").append(item.getQuantity())
                        .append(",\"price\":").append(item.getPrice()).append("}");
                validItems++;
            }
        }
        itemsJson.append("]");

        if (validItems == 0 || total <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "Keranjang kosong atau tidak valid", Snackbar.LENGTH_SHORT).show();
            return;
        }

        final double finalTotal = total;
        final String finalItemsJson = itemsJson.toString();

        new MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi Checkout")
                .setMessage("Total: Rp " + String.format("%,.0f", total) + "\n\nLanjutkan pembayaran?")
                .setPositiveButton("Bayar", (dialog, which) -> {
                    if (dbHelper != null && !userEmail.isEmpty()) {
                        boolean success = dbHelper.addTransaksiLayanan(userEmail, finalTotal, finalItemsJson);
                        if (success) {
                            showCheckoutSuccess();
                            cartItems.clear();
                            saveCartToPrefs();
                            updateCartUI();
                            if (bottomSheetCart.isShowing()) {
                                bottomSheetCart.dismiss();
                            }
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Gagal memproses pesanan", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Error: Data tidak valid", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCheckoutSuccess() {
        View successView = getLayoutInflater().inflate(R.layout.dialog_checkout_success, null);
        
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setView(successView)
                .setCancelable(false);
        
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        
        // Auto dismiss after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 2000);
    }
}
