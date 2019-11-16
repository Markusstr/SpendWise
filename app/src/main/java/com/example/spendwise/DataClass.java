package com.example.spendwise;

import android.graphics.Bitmap;

public class DataClass {
    private Bitmap productImage;
    private String productName;
    private String productPrice;

    DataClass(Bitmap productImage, String productName, String productPrice) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice= productPrice;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

}
