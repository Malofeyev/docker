package ru.malofeev.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import ru.malofeev.models.Stock;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InDataBaseStockDAO implements StockDAO{
    MarketDataBase database;

    public InDataBaseStockDAO(MarketDataBase database) {
        this.database = database;
    }

    public List<Stock> getStocks() {
        Stream<Document> rowStocks = StreamSupport.stream(
                database.getStocks().find().spliterator(), false
        );
        return rowStocks.map(Stock::new).collect(Collectors.toList());
    }

    public List<Stock> getStocksByNames(List<String> companyNames) {
        Stream<Document> rowStocks = StreamSupport.stream(
                database.getStocks().find(Filters.in("companyName", companyNames)).spliterator(), false
        );
        return rowStocks.map(Stock::new).collect(Collectors.toList());
    }

    public Stock getStock(String companyName) {
        return new Stock(
                database.getStocks().find(Filters.eq("companyName", companyName)).first()
        );
    }

    public boolean updateStock(String companyName, int count, int price) {
        database.getStocks().updateOne(Filters.eq("companyName", companyName),
                Updates.combine(Updates.set("count", count), Updates.set("price", price)));
        return true;
    }

    public boolean createStock(String companyName, int count, int price) {
        Stock stock = new Stock(companyName, count, price);
        database.getStocks().insertOne(stock.toDocument());
        return true;
    }

}
