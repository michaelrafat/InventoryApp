package com.example.android.inventoryapp;

/**
 * Created by miche on 7/6/2017.
 */

public class Product {

    private String name;
    private String price;
    private int quantity;

    public Product(String name, String price, int quantity) {

        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
