package ru.malofeev.dao;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;
import ru.malofeev.models.Stock;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class HttpStockDAO {
    private final String ADDRESS = "http://localhost:";
    private final int PORT = 8081;
    private final JsonParser parser = JsonParserFactory.getJsonParser();


    private String getResponse(String path, Map<String, Object> requestParameters) throws URISyntaxException, IOException, InterruptedException {
        String pathWithParameters = path;
        StringJoiner joiner = new StringJoiner("&");
        if (!requestParameters.isEmpty()) {
            for (Map.Entry<String, Object> entry: requestParameters.entrySet()) {
                StringBuilder parameter = new StringBuilder();
                parameter.append(entry.getKey());
                parameter.append("=");
                if (entry.getValue() instanceof List) {
                    parameter.append(String.join(",", (List<String>)entry.getValue()));
                } else {
                    parameter.append(entry.getValue().toString());
                }
                joiner.add(parameter);
            }
            pathWithParameters += "?" + joiner.toString();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ADDRESS + PORT + "/" + pathWithParameters))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public List<Stock> getStocks() throws URISyntaxException, IOException, InterruptedException {

        String response = getResponse("stocks", Map.of());
        System.out.println(response);
        return parser.parseList(response).stream()
                .map(obj->new Stock((Map<String, Object>)obj))
                .collect(Collectors.toList());
    }

    public List<Stock> getStocksByNames(List<String> companyNames) throws URISyntaxException, IOException, InterruptedException{
        String response = getResponse("stock", Map.of("companyNames", companyNames));
        return parser.parseList(response).stream()
                .map(obj->new Stock((Map<String, Object>)obj))
                .collect(Collectors.toList());
    }

    public boolean buyStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        String response = getResponse("buy/"+companyName, Map.of("count", count, "price", price));
        return true;
    }
    public boolean sellStock(String companyName, int count, int price) throws URISyntaxException, IOException, InterruptedException {
        String response = getResponse("sell/"+companyName, Map.of("count", count, "price", price));
        return true;
    }

}
