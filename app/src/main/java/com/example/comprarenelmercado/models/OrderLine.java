package com.example.comprarenelmercado.models;

import java.io.Serializable;

public class OrderLine implements Serializable {
    Product product;
    Float amount;
    String idstore;

    public OrderLine() {
    }

    public OrderLine(Product product, Float amount, String idstore) {
        this.product = product;
        this.amount = amount;
        this.idstore = idstore;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getStore() {
        return idstore;
    }

    public void setStore(String store) {
        this.idstore = store;
    }
}