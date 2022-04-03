package ru.malofeev.models;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class StockTest {
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("ya", 10, 10);
    }
    @Test
    void setPrice() {
        stock.setPrice(1);
        assertThat(stock.getPrice()).isEqualTo(1);
    }

    @Test
    void buy() {
        stock.buy(9, 10);
        assertThat(stock.getCount()).isEqualTo(1);
    }

    @Test
    void buyTooMany() {
        assertThatIllegalArgumentException().isThrownBy(()->stock.buy(11, 10));
    }

    @Test
    void sell() {
        stock.sell(1, 10);
        assertThat(stock.getCount()).isEqualTo(11);
    }
}