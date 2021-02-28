package com.example.comprarenelmercado.models;

import java.io.Serializable;

public class
Product implements Serializable {
    String description,productName,idProduct;
    Float stock, price;

    public Product() {
    }

    public Product(String idProduct,String productName, String description, float price, Float stock) {
        this.idProduct = idProduct;
        this.productName=productName;
        this.description = description;
        this.price = price;
        this.stock= stock;
    }

    public Product(String idProduct,String productName, float price) {
        this.idProduct = idProduct;
        this.productName=productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getStock() {
        return stock;
    }

    public void setStock(Float stock) {
        this.stock = stock;
    }
}
