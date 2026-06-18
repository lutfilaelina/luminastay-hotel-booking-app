package com.example.luminastay.models;

import java.util.List;

public class TransaksiLayanan {
    private int id;
    private String userEmail;
    private String tanggal;
    private double totalHarga;
    private String status; // "Pending", "Selesai", "Dibatalkan"
    private List<ItemLayanan> items;

    public TransaksiLayanan() {
    }

    public TransaksiLayanan(int id, String userEmail, String tanggal, double totalHarga, String status) {
        this.id = id;
        this.userEmail = userEmail;
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemLayanan> getItems() {
        return items;
    }

    public void setItems(List<ItemLayanan> items) {
        this.items = items;
    }

    public String getFormattedTotal() {
        return "Rp " + String.format("%,.0f", totalHarga);
    }

    // Inner class for cart items
    public static class ItemLayanan {
        private int layananId;
        private String layananName;
        private int quantity;
        private double price;

        public ItemLayanan(int layananId, String layananName, int quantity, double price) {
            this.layananId = layananId;
            this.layananName = layananName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getLayananId() {
            return layananId;
        }

        public void setLayananId(int layananId) {
            this.layananId = layananId;
        }

        public String getLayananName() {
            return layananName;
        }

        public void setLayananName(String layananName) {
            this.layananName = layananName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getSubtotal() {
            return quantity * price;
        }
    }
}

