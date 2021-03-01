package com.example.comprarenelmercado.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    static int nextID=0;
    int orderId;
    ArrayList<OrderLine> orderLines = new ArrayList<>();
    Date orderDate;
    int status;
    String UserID = "";

    public Order() {
        this.orderId= nextID;
        nextID++;
    }

    public Order(ArrayList<OrderLine> orderLines, Date orderDate) {
        this.orderId= nextID;
        nextID++;
        this.orderLines = orderLines;
        this.orderDate = orderDate;
        this.status=0;
    }

    public Order(Date orderDate) {
        this.orderId= nextID;
        nextID++;
        this.orderLines = new ArrayList<>();
        this.orderDate = orderDate;
        this.status=0;
    }

    public int getOrderId() {
        return orderId;
    }

    public  ArrayList<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines( ArrayList<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void addLine(OrderLine line) {
        orderLines.add(line);
    }
}
