package com.example.luminastay.models;

public class Layanan {
    private int id;
    private String name;
    private String category; // "Makanan & Minuman" atau "Tambahan Kamar"
    private double price;
    private String description;
    private int imageResource;

    public Layanan() {
    }

    public Layanan(int id, String name, String category, double price, String description, int imageResource) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getFormattedPrice() {
        return "Rp " + String.format("%,.0f", price);
    }
}

