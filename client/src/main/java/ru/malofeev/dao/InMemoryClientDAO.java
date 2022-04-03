package ru.malofeev.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.malofeev.models.Client;
import ru.malofeev.models.Stock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryClientDAO implements ClientDAO{
    List<Client> clients;
    HttpStockDAO httpClientDAO;

    @Autowired
    public InMemoryClientDAO(HttpStockDAO httpClientDAO) {
        this.httpClientDAO = httpClientDAO;

        Map<String, Integer> stockCounts1 = new HashMap<>();
        stockCounts1.put("ya", 1);
        Map<String, Integer> stockCounts2 = new HashMap<>();
        stockCounts2.put("ya", 2);
        stockCounts2.put("google", 11);
        Map<String, Integer> stockCountsEmpty = new HashMap<>();
        List<Stock> stocks = new ArrayList<>(Arrays.asList(
                new Stock("ya", 100, 1),
                new Stock("google", 100, 2),
                new Stock("microsoft", 100, 3)
        ));

        clients = new ArrayList<>(Arrays.asList(
                new Client(1, 12, stockCounts1, new ArrayList<>(stocks)),
                new Client(2, 11, stockCounts2, new ArrayList<>(stocks)),
                new Client(3, 100, stockCountsEmpty, new ArrayList<>(stocks))
        ));
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getClient(int id) throws IllegalArgumentException {
        return clients.stream().filter(client -> client.getId() == id)
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("Client doesn't exist")
                );
    }

    public List<Stock> getStocks() throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.getStocks();
    }

    public Stock getStock(String companyName) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.getStocksByNames(List.of(companyName)).stream().findFirst().orElse(null);
    }

    public boolean createClient(int id, int money) {
        Client client = new Client(id, money, new HashMap<>(), new ArrayList<>());
        clients.add(client);
        return true;
    }

    public boolean buyStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.buyStock(companyName, count, price);
    }

    public boolean sellStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.sellStock(companyName, count, price);
    }

    public boolean updateClient(int id, int money, List<Document> stockCounts) throws URISyntaxException, IOException, InterruptedException {
        Client client = getClient(id);
        client.setMoney(money);
        Map<String, Integer> mapStockCount = stockCounts.stream()
                .collect(Collectors.toMap(d->d.getString("companyName"), d-> d.getInteger("count")));
        client.setStockCounts(mapStockCount);
        return true;
    }


}
