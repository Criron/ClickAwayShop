package com.example.polit.clickawayshop;

import java.util.List;

public class Cart {
    private List<Book> books;
    private String userId;

    public Cart() {}

    public Cart(List<Book> books, String userId) {
        this.books = books;
        this.userId = userId;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
