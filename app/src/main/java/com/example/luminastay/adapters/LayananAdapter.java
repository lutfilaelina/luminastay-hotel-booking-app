package com.example.luminastay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.models.Layanan;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.LayananViewHolder> {
    private Context context;
    private List<Layanan> layananList;
    private OnAddToCartListener listener;

    public interface OnAddToCartListener {
        void onAddToCart(Layanan layanan);
    }

    public LayananAdapter(Context context, List<Layanan> layananList, OnAddToCartListener listener) {
        this.context = context;
        this.layananList = layananList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LayananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layanan_grid, parent, false);
        return new LayananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayananViewHolder holder, int position) {
        Layanan layanan = layananList.get(position);

        holder.tvLayananName.setText(layanan.getName());
        holder.tvLayananCategory.setText(layanan.getCategory());
        holder.tvLayananPrice.setText(layanan.getFormattedPrice());
        holder.tvLayananDescription.setText(layanan.getDescription());
        holder.ivLayananImage.setImageResource(layanan.getImageResource());

        // Show popular badge for certain items
        if (layanan.getId() <= 3) { // First 3 items are popular
            holder.badgePopular.setVisibility(View.VISIBLE);
        } else {
            holder.badgePopular.setVisibility(View.GONE);
        }

        // Show delivery time for food items
        if (layanan.getCategory().contains("Makanan") || layanan.getCategory().contains("Minuman")) {
            holder.deliveryTimeContainer.setVisibility(View.VISIBLE);
            holder.tvDeliveryTime.setText("~ 15 menit");
        } else {
            holder.deliveryTimeContainer.setVisibility(View.GONE);
        }

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCart(layanan);
                // Animation will be handled in activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }

    public static class LayananViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLayananImage;
        TextView tvLayananName, tvLayananCategory, tvLayananPrice, tvLayananDescription, tvDeliveryTime;
        MaterialButton btnAddToCart;
        View badgePopular, deliveryTimeContainer;

        public LayananViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLayananImage = itemView.findViewById(R.id.ivLayananImage);
            tvLayananName = itemView.findViewById(R.id.tvLayananName);
            tvLayananCategory = itemView.findViewById(R.id.tvLayananCategory);
            tvLayananPrice = itemView.findViewById(R.id.tvLayananPrice);
            tvLayananDescription = itemView.findViewById(R.id.tvLayananDescription);
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            badgePopular = itemView.findViewById(R.id.badgePopular);
            deliveryTimeContainer = itemView.findViewById(R.id.deliveryTimeContainer);
        }
    }
}

