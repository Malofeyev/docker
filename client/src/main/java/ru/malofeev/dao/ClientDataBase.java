package ru.malofeev.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;

@Component
public class ClientDataBase implements Closeable {
    private static final String DBNAME = "Client";
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> clients;

    public MongoCollection<Document> getClients() {
        return clients;
    }

    @Autowired
    public ClientDataBase() {
        mongoClient = MongoClients.create();
        database = mongoClient.getDatabase(DBNAME);
        clients = database.getCollection("Clients");
    }


    @Override
    @PreDestroy
    public void close() throws IOException {
        mongoClient.close();
    }
}
