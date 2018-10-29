package com.example.a213506699.foodordering;
public class MenuItem {
    private int menu_ID;
    private int category_ID;
    private String description;
    private double price;
    private String item_size;

    public MenuItem() {
    }

    public MenuItem(int menu_ID, int category_ID, String description, double price, String item_size) {
        this.menu_ID = menu_ID;
        this.category_ID = category_ID;
        this.description = description;
        this.price = price;
        this.item_size = item_size;
    }

    public void setMenu_ID(int menu_ID) {
        this.menu_ID = menu_ID;
    }

    public void setCategory_ID(int category_ID) {
        this.category_ID = category_ID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }

    public int getMenu_ID() {
        return menu_ID;
    }

    public int getCategory_ID() {
        return category_ID;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getItem_size() {
        return item_size;
    }
}
