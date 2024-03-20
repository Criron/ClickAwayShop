package com.example.polit.clickawayshop;

import java.util.Map;

public class Stock {
    private Map<String, Integer> availableItems;
    private String storeId;

    public Stock () {}

    public Stock(Map<String, Integer> availableItems, String storeId) {
        this.availableItems = availableItems;
        this.storeId = storeId;
    }

    public Map<String, Integer> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(Map<String, Integer> availableItems) {
        this.availableItems = availableItems;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
