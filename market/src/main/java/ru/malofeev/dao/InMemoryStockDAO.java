package ru.malofeev.dao;

import org.springframework.stereotype.Component;
import ru.malofeev.models.Stock;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryStockDAO implements StockDAO{

    private final List<Stock> stocks;

    {
        stocks = new ArrayList<>(Arrays.asList(
                new Stock("ya", 100, 1),
                new Stock("google", 100, 2),
                new Stock("microsoft", 100, 3)
        ));
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Stock> getStocksByNames(List<String> companyNames) {
        Set<String> companyNamesSet = new HashSet<>(companyNames);
        return stocks.stream().filter(s->companyNamesSet.contains(s.getCompanyName()))
                .collect(Collectors.toList());
    }

    public Stock getStock(String companyName) {
        return stocks.stream().filter(s -> s.getCompanyName().equals(companyName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Stock doesn't exist"));
    }

    public boolean updateStock(String companyName, int count, int price) {
        Stock stock = getStock(companyName);
        stock.setCount(count);
        stock.setPrice(price);
        return true;
    }

    public boolean createStock(String companyName, int count, int price) {
        Stock stock = new Stock(companyName, count, price);
        for(Stock s: stocks) {
            if (s.getCompanyName().equals(companyName)) {
                s.setPrice(price);
                s.setCount(count);
                return true;
            }
        }
        stocks.add(stock);
        return true;
    }
}
