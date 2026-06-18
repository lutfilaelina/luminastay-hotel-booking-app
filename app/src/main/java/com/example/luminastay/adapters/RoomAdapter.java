package com.example.luminastay.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luminastay.R;
import com.example.luminastay.activities.RoomDetailActivity;
import com.example.luminastay.database.DBHelper;
import com.example.luminastay.models.Room;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private Context context;
    private List<Room> roomList;
    private String userEmail;
    private DBHelper dbHelper;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
        SharedPreferences prefs = context.getSharedPreferences("LuminaStayPrefs", Context.MODE_PRIVATE);
        this.userEmail = prefs.getString("userEmail", "");
        this.dbHelper = new DBHelper(context);
    }

    public RoomAdapter(Context context, List<Room> roomList, String userEmail, DBHelper dbHelper) {
        this.context = context;
        this.roomList = roomList;
        this.userEmail = userEmail;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());
        holder.tvRoomType.setText(room.getType());
        holder.tvRoomPrice.setText(room.getFormattedPrice());
        holder.ivRoomImage.setImageResource(room.getImageResource());

        // Rating
        if (room.getRating() > 0) {
            holder.tvRating.setText(String.format("%.1f", room.getRating()));
            holder.tvReviewCount.setText("(" + room.getReviewCount() + ")");
            holder.tvRating.setVisibility(View.VISIBLE);
            holder.tvReviewCount.setVisibility(View.VISIBLE);
        } else {
            holder.tvRating.setText("0.0");
            holder.tvReviewCount.setText("(0)");
        }

        // Favorite button
        boolean isFavorite = dbHelper.isFavorite(userEmail, room.getId());
        updateFavoriteIcon(holder.btnFavorite, isFavorite);

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFav = dbHelper.isFavorite(userEmail, room.getId());
                if (isFav) {
                    dbHelper.removeFavorite(userEmail, room.getId());
                    updateFavoriteIcon(holder.btnFavorite, false);
                    Snackbar.make(v, "Dihapus dari favorit", Snackbar.LENGTH_SHORT).show();
                } else {
                    dbHelper.addFavorite(userEmail, room.getId());
                    updateFavoriteIcon(holder.btnFavorite, true);
                    Snackbar.make(v, "Ditambahkan ke favorit", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // Detail button click
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomDetailActivity.class);
                intent.putExtra("roomId", room.getId());
                intent.putExtra("roomName", room.getName());
                intent.putExtra("roomType", room.getType());
                intent.putExtra("roomDescription", room.getDescription());
                intent.putExtra("roomPrice", room.getPricePerNight());
                intent.putExtra("roomImage", room.getImageResource());
                intent.putExtra("roomFacilities", room.getFacilities());
                intent.putExtra("roomRating", room.getRating());
                intent.putExtra("roomReviewCount", room.getReviewCount());
                context.startActivity(intent);
            }
        });
    }

    private void updateFavoriteIcon(ImageButton btnFavorite, boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_heart);
            btnFavorite.setColorFilter(context.getResources().getColor(R.color.error));
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_heart_outline);
            btnFavorite.setColorFilter(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRoomImage;
        ImageButton btnFavorite;
        TextView tvRoomName, tvRoomType, tvRoomPrice, tvRating, tvReviewCount;
        MaterialButton btnDetail;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRoomImage = itemView.findViewById(R.id.ivRoomImage);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}