package com.example.luminastay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.models.TransaksiLayanan;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TransaksiLayananAdapter extends RecyclerView.Adapter<TransaksiLayananAdapter.TransaksiViewHolder> {
    private Context context;
    private List<TransaksiLayanan> transaksiList;

    public TransaksiLayananAdapter(Context context, List<TransaksiLayanan> transaksiList) {
        this.context = context;
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_layanan, parent, false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int position) {
        TransaksiLayanan transaksi = transaksiList.get(position);

        holder.tvTanggal.setText(transaksi.getTanggal());
        holder.tvTotal.setText(transaksi.getFormattedTotal());
        holder.tvStatus.setText(transaksi.getStatus());
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static class TransaksiViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvTanggal, tvTotal, tvStatus;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

