package com.example.mygrocerystore.adminModels;

public class OrderModel {
    String orderId;
    String current_date;
    String latitude;
    String longitude;
    String status;
    String userId;
    String user_address;
    String user_email;
    String user_name;
    String user_phone;

    public OrderModel() {
    }

    public OrderModel(String orderId, String current_date, String latitude, String longitude, String status, String userId, String user_address, String user_email, String user_name, String user_phone) {
        this.orderId = orderId;
        this.current_date = current_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.userId = userId;
        this.user_address = user_address;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
