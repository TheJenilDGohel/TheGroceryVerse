package com.example.mygrocerystore.models;

public class UserOrderModel {
    String orderId;
    String orderDate;
    String status;
    String userId;
    String user_name;
    String user_address;
    String user_phone;
    String longitude;
    String latitude;
    String documentId;


    public UserOrderModel() {
    }

    public UserOrderModel(String orderId, String orderDate, String status, String userId, String user_name, String user_address, String user_phone, String longitude, String latitude) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.userId = userId;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
