package ru.malofeev.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.malofeev.models.Client;
import ru.malofeev.models.Stock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class InDataBaseClientDAO implements ClientDAO {
    private final ClientDataBase database;
    private final HttpStockDAO httpClientDAO;

    @Autowired
    public InDataBaseClientDAO(ClientDataBase database, HttpStockDAO httpClientDAO) {
        this.database = database;
        this.httpClientDAO = httpClientDAO;
    }

    public List<Client> getClients() throws URISyntaxException, IOException, InterruptedException {
        List<Stock> stocks = httpClientDAO.getStocks();
        Stream<Document> rowClients = StreamSupport.stream(
                database.getClients().find().spliterator(), false
        );
        return rowClients.map(doc->new Client(doc, stocks)).collect(Collectors.toList());
    }

    public Client getClient(int id) throws URISyntaxException, IOException, InterruptedException {
        Document doc = database.getClients().find(Filters.eq("id", id)).first();
        List<String> companyNames = doc.getList("stockCounts", Document.class)
                .stream()
                .map(d->d.getString("companyName"))
                .collect(Collectors.toList());
        List<Stock> stocks = httpClientDAO.getStocksByNames(companyNames);
        return new Client(doc, stocks);
    }

    public boolean createClient(int id, int money) {
        Client client = new Client(id, money, new HashMap<String, Integer>(), new ArrayList<Stock>());
        database.getClients().insertOne(client.toDocument());
        return true;
    }

    public boolean buyStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.buyStock(companyName, count, price);
    }

    public boolean sellStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.sellStock(companyName, count, price);
    }


    public boolean updateClient(int id, int money, List<Document> stockCounts) throws URISyntaxException, IOException, InterruptedException {
        database.getClients().updateOne(Filters.eq("id", id),
                Updates.combine(Updates.set("money", money), Updates.set("stockCounts", stockCounts)));
        return true;
    }

    public Stock getStock(String companyName) throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.getStocksByNames(List.of(companyName)).stream().findFirst().orElse(null);
    }

    public List<Stock> getStocks() throws URISyntaxException, IOException, InterruptedException {
        return httpClientDAO.getStocks();
    }
}
