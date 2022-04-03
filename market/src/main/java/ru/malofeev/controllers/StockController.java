package ru.malofeev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.malofeev.dao.StockDAO;
import ru.malofeev.models.Stock;

import java.util.List;

@RestController
public class StockController {
    private final StockDAO stockDAO;

    @Autowired
    public StockController(@Qualifier("inMemoryStockDAO") StockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    @RequestMapping("/stocks")
    public List<Stock> getStocks() {
        return stockDAO.getStocks();
    }

    @RequestMapping("/stock")
    public List<Stock> getChosenStocks(@RequestParam List<String> companyNames) {
        return stockDAO.getStocksByNames(companyNames);
    }

    @RequestMapping("/changePrice/{companyName}")
    public String changePrice(@PathVariable String companyName, @RequestParam int price) {
        Stock stock = stockDAO.getStock(companyName);
        stock.setPrice(price);
        stockDAO.updateStock(stock.getCompanyName(), stock.getCount(), price);
        return "true";
    }

    @RequestMapping("/buy/{companyName}")
    public String buyStocks(@PathVariable String companyName,
                            @RequestParam int count,
                            @RequestParam int price) {
        Stock stock = stockDAO.getStock(companyName);
        stock.buy(count, price);
        stockDAO.updateStock(stock.getCompanyName(), stock.getCount(), stock.getPrice());
        return "true";
    }

    @RequestMapping("/sell/{companyName}")
    public String sellStocks(@PathVariable String companyName,
                            @RequestParam int count,
                            @RequestParam int price) {
        Stock stock = stockDAO.getStock(companyName);
        stock.sell(count, price);
        stockDAO.updateStock(stock.getCompanyName(), stock.getCount(), stock.getPrice());
        return "true";
    }

    @RequestMapping("/add_stock")
    public String addStock(@RequestParam String companyName,
                           @RequestParam int count,
                           @RequestParam int price) {
        stockDAO.createStock(companyName, count, price);
        return "true";

    }
}
