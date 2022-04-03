package ru.malofeev.models;

import org.bson.Document;

public interface MongoDocument {
    Document toDocument();
}
