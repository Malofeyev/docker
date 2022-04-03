package ru.malofeev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.malofeev.dao.ClientDAO;
import ru.malofeev.models.Client;
import ru.malofeev.models.Stock;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class ClientController {

    private final ClientDAO clientDAO;

    @Autowired
    public ClientController(@Qualifier("inMemoryClientDAO") ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @RequestMapping("/people")
    public String getClients() throws URISyntaxException, IOException, InterruptedException {
        return clientDAO.getClients().toString();
    }

    @RequestMapping("/people/{id}")
    public String getClient(@PathVariable("id") int id) throws URISyntaxException, IOException, InterruptedException {
        return clientDAO.getClient(id).toString();
    }

    @RequestMapping("/people/{id}/buy_stock")
    public String buyStock(@PathVariable("id") int id, @RequestParam("companyName") String companyName,
                           @RequestParam("count") int count) throws URISyntaxException, IOException, InterruptedException {
        Client client = clientDAO.getClient(id);
        if (client.getStock(companyName) == null) {
            client.addStockInfo(clientDAO.getStock(companyName));
        }
        try {
            client.buyStock(companyName, count);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        Stock stock = client.getStock(companyName);
        clientDAO.buyStock(stock.getCompanyName(), count, stock.getPrice());
        return String.valueOf(clientDAO.updateClient(client.getId(),
                client.getMoney(),
                client.getStockCountsDocument()));
    }

    @RequestMapping("/people/{id}/sell_stock")
    public String sellStock(@PathVariable("id") int id, @RequestParam("companyName") String companyName,
                           @RequestParam("count") int count) throws URISyntaxException, IOException, InterruptedException {
        Client client = clientDAO.getClient(id);
        try {
            client.sellStock(companyName, count);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        Stock stock = client.getStock(companyName);
        clientDAO.sellStock(stock.getCompanyName(), count, stock.getPrice());
        return String.valueOf(clientDAO.updateClient(client.getId(),
                client.getMoney(),
                client.getStockCountsDocument()));
    }

    @RequestMapping("/people/new")
    public String createClients(@RequestParam("id") int id,
                             @RequestParam(name="money", required = false) int money) {
        return Boolean.toString(clientDAO.createClient(id, money));
    }

    @RequestMapping("/")
    public String getStocks() throws URISyntaxException, IOException, InterruptedException {
        return clientDAO.getStocks().toString();
    }

}
