package com.example.a213506699.foodordering;

public class OrderItem {
    String date;
    String description;
    double price;
    int quantity;
    public OrderItem(String date, String description, double price, int quantity) {
        this.date = date;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {

    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
