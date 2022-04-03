package ru.malofeev.dao;

import org.bson.Document;
import ru.malofeev.models.Client;
import ru.malofeev.models.Stock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ClientDAO {
    List<Client> getClients() throws URISyntaxException, IOException, InterruptedException;
    Client getClient(int id) throws URISyntaxException, IOException, InterruptedException;
    boolean createClient(int id, int money);
    boolean buyStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException;
    boolean sellStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException;
    boolean updateClient(int id, int money, List<Document> stockCounts) throws URISyntaxException, IOException, InterruptedException;
    Stock getStock(String companyName) throws URISyntaxException, IOException, InterruptedException;
    List<Stock> getStocks() throws URISyntaxException, IOException, InterruptedException;
}
