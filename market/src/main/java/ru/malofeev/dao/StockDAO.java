package ru.malofeev.dao;

import ru.malofeev.models.Stock;

import java.util.List;

public interface StockDAO {
    List<Stock> getStocks();
    List<Stock> getStocksByNames(List<String> companyNames);
    Stock getStock(String companyNames);
    boolean updateStock(String companyName, int count, int price);
    boolean createStock(String companyName, int count, int price);

}
