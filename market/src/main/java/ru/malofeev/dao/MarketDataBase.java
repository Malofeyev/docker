package ru.malofeev.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;


public class MarketDataBase implements Closeable {
    private static final String DBNAME = "Market";
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> stocks;

    public MongoCollection<Document> getStocks() {
        return stocks;
    }

    public MarketDataBase() {
        mongoClient = MongoClients.create("mongodb://admin:pwd@mongodb:27017");
        database = mongoClient.getDatabase(DBNAME);
        stocks = database.getCollection("Stocks");
    }

    public void close() throws IOException {
        mongoClient.close();
    }
}
