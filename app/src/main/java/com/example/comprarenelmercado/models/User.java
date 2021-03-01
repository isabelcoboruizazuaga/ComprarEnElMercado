package com.example.comprarenelmercado.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String userName, email, uid;
    private Order cart;
    private List<Order> orders= new ArrayList<>();
    private float balance;
    private boolean isManager;

    public User (){

    }
    public User(String email, String userName, String uid){
        this.email= email;
        this.userName=userName;
        this.uid=uid;
        this.balance = 0;
        isManager=false;
        cart=new Order();
    }

    public void addOrder(Order order){
            orders.add(order);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance() { return balance; }

    public Order getCart() {
        return cart;
    }

    public void setCart(Order cart) {
        this.cart = cart;
    }

    public void addProductToCart(OrderLine orderLine) {
        cart.addLine(orderLine);
    }

    public boolean isManager(){return isManager;}
    public void  makeManager(){isManager=true;}

}
