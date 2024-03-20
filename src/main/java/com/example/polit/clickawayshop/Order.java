package com.example.polit.clickawayshop;

import java.sql.Timestamp;

public class Order {
    private Book[] books;
    private String storeId;
    private String userId;
    private Timestamp clickAwayAppointment;

    public Order() {}

    public Order(Book[] books, String storeId, String userId, Timestamp clickAwayAppointment) {
        this.books = books;
        this.storeId = storeId;
        this.userId = userId;
        this.clickAwayAppointment = clickAwayAppointment;
    }

    public Book[] getBooks() {
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getClickAwayAppointment() {
        return clickAwayAppointment;
    }

    public void setClickAwayAppointment(Timestamp clickAwayAppointment) {
        this.clickAwayAppointment = clickAwayAppointment;
    }
}
