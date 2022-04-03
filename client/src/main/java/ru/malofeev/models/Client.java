package ru.malofeev.models;


import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Client implements MongoDocument{

    private final int id;
    private int money;
    private Map<String, Integer> stockCounts;
    private List<Stock> stocks;

    public Client(int id, int money, Map<String, Integer> stockCounts, List<Stock> stocks) {
        this.id = id;
        this.money = money;
        this.stockCounts = stockCounts;
        this.stocks = stocks;
    }

    public Client(Document document, List<Stock> stocks) {
        id = document.getInteger("id");
        money = document.getInteger("money");
        List<Document> stockCountsList = document.getList("stockCounts", Document.class);
        stockCounts = stockCountsList.stream()
                .collect(Collectors.toMap(doc->doc.getString("companyName"), doc->doc.getInteger("count")));
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public Map<String, Integer> getStockCounts() {
        return stockCounts;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public Stock getStock(String companyName) {
        return stocks.stream().filter(s -> s.getCompanyName().equals(companyName))
                .findAny().orElse(null);
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setStockCount(String companyName, int count) {
        if (count == 0) {
            stockCounts.remove(companyName);
        } else {
            stockCounts.put(companyName, count);
        }
    }

    public void setStockCounts(Map<String, Integer> stockCounts) {
        this.stockCounts = stockCounts;
    }

    public void addStockInfo(Stock stock) {
        stocks.add(stock);
    }

    public void buyStock(String companyName, int expectedCount) throws IllegalArgumentException {
        Stock stock = stocks.stream().filter(s -> s.getCompanyName().equals(companyName))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("Stock doesn't exist")
                );
        if (stock.getPrice() * expectedCount > money) {
            throw new IllegalArgumentException("Don't have enough money");
        }
        if (expectedCount > stock.getCount()) {
            throw new IllegalArgumentException("Don't have enough stocks");
        }
        setMoney(money - stock.getPrice() * expectedCount);

        setStockCount(companyName, expectedCount + stockCounts.getOrDefault(companyName, 0));
        stock.setCount(stock.getCount() - expectedCount);

    }

    public void sellStock(String companyName, int expectedCount) throws IllegalArgumentException {
        Stock stock = stocks.stream().filter(s -> s.getCompanyName().equals(companyName))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("Stock doesn't exist")
                );
        int count = stockCounts.getOrDefault(companyName, 0);

        if (count < expectedCount) {
            throw new IllegalArgumentException("Don't have enough stocks");
        }

        setMoney(money + expectedCount * stock.getPrice());
        setStockCount(companyName, count - expectedCount);
        stock.setCount(stock.getCount() + expectedCount);
    }

    public int getAllMoney() {
        return stocks.stream().map(
                s->s.getPrice() * stockCounts.getOrDefault(s.getCompanyName(), 0)
        ).reduce(0, Integer::sum) + money;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", money=" + money +
                ", stockCounts=" + stockCounts +
                ", stocks=" + stocks +
                '}';
    }

    @Override
    public Document toDocument() {
        return new Document().append("id", id).append("money", money)
                .append("stockCounts", getStockCountsDocument());
    }

    public List<Document> getStockCountsDocument() {
        return stockCounts.entrySet().stream()
                .map(entry->new Document()
                        .append("companyName", entry.getKey())
                        .append("count", entry.getValue())
                ).collect(Collectors.toList());
    }

}
