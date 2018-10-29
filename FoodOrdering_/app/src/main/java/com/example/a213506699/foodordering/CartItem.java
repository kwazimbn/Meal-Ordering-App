package com.example.a213506699.foodordering;

public class CartItem {
    int cartID,menuItemID,shopID,quantity;
    double price;
    String description;

    public CartItem() {

    }

    public CartItem(int cartID, int menuItemID, int shopID, String description, int quantity, double price) {
        this.cartID = cartID;
        this.menuItemID = menuItemID;
        this.shopID = shopID;
        this.quantity = quantity;
        this.price = price;
        this.description = description;

    } public int getCartID() {
        return cartID;
    }

    public String getDescription() {
        return description;
    }



    public int getMenuItemID() {
        return menuItemID;
    }

    public int getShopID() {
        return shopID;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
