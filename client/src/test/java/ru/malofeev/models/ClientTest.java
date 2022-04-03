package ru.malofeev.models;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ClientTest {
    private Client client;
    private final static List<Stock> stocks = Arrays.asList(
            new Stock("ya", 100, 1),
            new Stock("google", 100, 2),
            new Stock("microsoft", 100, 3)
    );

    @BeforeEach
    void setUp() {
        Map<String, Integer> stockCounts = new HashMap<>();
        stockCounts.put("ya", 1);
        client = new Client(1, 12, stockCounts, stocks);
    }

    @Test
    void setStockZeroCount() {
        client.setStockCount("ya", 0);
        assertThat("ya").isNotIn(client.getStockCounts().keySet());
    }

    @Test
    void setStockCount() {
        client.setStockCount("ya", 10);
        assertThat("ya").isIn(client.getStockCounts().keySet());
    }

    @Test
    void buyStock() {
        client.buyStock("ya", 10);
        assertThat(client.getMoney()).isEqualTo(2);
    }

    @Test
    void buyNewStock() {
        client.buyStock("google", 6);
        assertThat(client.getMoney()).isEqualTo(0);
        assertThat("google").isIn(client.getStockCounts().keySet());
    }

    @Test
    void sellStock() {
        client.sellStock("ya", 1);
        assertThat(client.getMoney()).isEqualTo(13);
        assertThat("ya").isNotIn(client.getStockCounts().keySet());
    }

    @Test
    void sellTooManyStocks() {
        assertThatIllegalArgumentException().isThrownBy(()->client.sellStock("ya", 2));

    }

    @Test
    void getAllMoney() {
        assertThat(client.getAllMoney()).isEqualTo(13);
    }

    @Test
    void getAllMoneyEmptyStocks() {
        Client client = new Client(1, 1, new HashMap<String, Integer>(), stocks);
        assertThat(client.getAllMoney()).isEqualTo(1);
    }

}