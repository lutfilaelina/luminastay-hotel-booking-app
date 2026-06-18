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
import com.example.luminastay.models.TransaksiLayanan;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<TransaksiLayanan.ItemLayanan> cartItems;
    private List<Layanan> layananList;
    private OnCartItemChangedListener listener;

    public interface OnCartItemChangedListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemRemoved(int position);
    }

    public CartAdapter(Context context, List<TransaksiLayanan.ItemLayanan> cartItems, 
                      List<Layanan> layananList, OnCartItemChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.layananList = layananList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TransaksiLayanan.ItemLayanan item = cartItems.get(position);
        
        // Find layanan info
        Layanan layanan = findLayananById(item.getLayananId());
        
        if (layanan != null) {
            holder.ivCartItemImage.setImageResource(layanan.getImageResource());
        }
        
        holder.tvCartItemName.setText(item.getLayananName());
        holder.tvCartItemPrice.setText("Rp " + String.format("%,.0f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            if (listener != null) {
                listener.onQuantityChanged(position, newQuantity);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                if (listener != null) {
                    listener.onQuantityChanged(position, newQuantity);
                }
            } else {
                if (listener != null) {
                    listener.onItemRemoved(position);
                }
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(position);
            }
        });
    }

    private Layanan findLayananById(int id) {
        if (layananList != null) {
            for (Layanan layanan : layananList) {
                if (layanan.getId() == id) {
                    return layanan;
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCartItemImage;
        TextView tvCartItemName, tvCartItemPrice, tvQuantity;
        MaterialButton btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartItemImage = itemView.findViewById(R.id.ivCartItemImage);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}

