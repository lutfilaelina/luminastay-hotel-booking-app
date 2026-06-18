package com.example.luminastay.models;

public class Room {
    private int id;
    private String name;
    private String type;
    private String description;
    private double pricePerNight;
    private int imageResource;
    private String facilities;
    private double rating;
    private int reviewCount;

    public Room() {
    }

    public Room(int id, String name, String type, String description,
                double pricePerNight, int imageResource, String facilities) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.imageResource = imageResource;
        this.facilities = facilities;
        this.rating = 0.0;
        this.reviewCount = 0;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getFormattedPrice() {
        return "Rp " + String.format("%,.0f", pricePerNight);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}