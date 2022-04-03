package ru.malofeev.models;

import org.bson.Document;

import java.util.Map;

public class Stock {
    private final String companyName;
    private int count;
    private final int price;

    public Stock(String companyName, int count, int price) {
        this.companyName = companyName;
        this.count = count;
        this.price = price;
    }

    public Stock(Document document) {
        companyName = document.getString("companyName");
        count = document.getInteger("count");
        price = document.getInteger("price");
    }

    public Stock(Map<String, Object> stockFields) {
        companyName = (String)stockFields.get("companyName");
        count = (int)stockFields.get("count");
        price = (int)stockFields.get("price");
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "companyName='" + companyName + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
