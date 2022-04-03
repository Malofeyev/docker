package ru.malofeev.models;

import org.bson.Document;

public class Stock implements MongoDocument
{
    private final String companyName;
    private int count;
    private int price;

    public Stock(String companyName, int count, int price) {
        this.companyName = companyName;
        this.count = count;
        this.price = price;
    }

    public Stock(Document document) {
        companyName = document.getString("companyName");
        count = document.getDouble("count").intValue();
        price = document.getDouble("price").intValue();
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void buy(int count, int price) throws IllegalArgumentException {
        if (price != this.price) {
            throw new IllegalArgumentException("Your information is out of date");
        }
        if (count > this.count) {
            throw new IllegalArgumentException("Too many bought stocks");
        }
        setCount(this.count - count);
    }

    public void sell(int count, int price) throws IllegalArgumentException{
        if (price != this.price) {
            throw new IllegalArgumentException("Your information is out of date");
        }
        setCount(this.count + count);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "companyName='" + companyName + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("companyName", companyName)
                .append("count", count)
                .append("price", price);
    }
}
