package integration;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import ru.malofeev.dao.ClientDAO;
import ru.malofeev.dao.HttpStockDAO;
import ru.malofeev.dao.InMemoryClientDAO;
import ru.malofeev.models.Client;
import ru.malofeev.models.Stock;

import static org.assertj.core.api.Assertions.*;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientTest {
    @ClassRule
    public static GenericContainer marketContainer = new FixedHostPortGenericContainer("market:1.0-SNAPSHOT")
            .withFixedExposedPort(8081, 8080)
            .withExposedPorts(8080);

    private ClientDAO clientDAO;

    private final static String TEST_COMPANY_NAME = "test_company";
    private final static int TEST_COMPANY_STOCKS_COUNT = 10;
    private final static int TEST_COMPANY_STOCKS_PRICE = 1;
    private final static int TEST_USER_ID = 10;
    private final static int TEST_USER_INITIAL_MONEY = 1;

    @Before
    public void setUp() throws Exception {
        marketContainer.start();
        clientDAO = new InMemoryClientDAO(new HttpStockDAO());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/add_stock?companyName=" + TEST_COMPANY_NAME +
                        "&count=" + TEST_COMPANY_STOCKS_COUNT + "&price=" + TEST_COMPANY_STOCKS_PRICE))
                .GET()
                .build();

        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ClientDAO clientDAO = new InMemoryClientDAO(new HttpStockDAO());
        clientDAO.createClient(TEST_USER_ID, TEST_USER_INITIAL_MONEY);
    }

    @Test
    public void getStock() throws URISyntaxException, IOException, InterruptedException {
        Stock stock = clientDAO.getStock(TEST_COMPANY_NAME);
        assertThat(stock.getCount()).isEqualTo(TEST_COMPANY_STOCKS_COUNT);
        assertThat(stock.getPrice()).isEqualTo(TEST_COMPANY_STOCKS_PRICE);
    }

    @Test
    public void buyStock() throws URISyntaxException, IOException, InterruptedException {
        Stock stockBefore = clientDAO.getStock(TEST_COMPANY_NAME);
        clientDAO.buyStock(TEST_COMPANY_NAME, 1, 1);
        Stock stockAfter = clientDAO.getStock(TEST_COMPANY_NAME);
        assertThat(stockBefore.getCount()).isEqualTo(stockAfter.getCount() + 1);
    }

    @Test
    public void sellStock() throws URISyntaxException, IOException, InterruptedException {
        Stock stockBefore = clientDAO.getStock(TEST_COMPANY_NAME);
        clientDAO.sellStock(TEST_COMPANY_NAME, 1, 1);
        Stock stockAfter = clientDAO.getStock(TEST_COMPANY_NAME);
        assertThat(stockBefore.getCount()).isEqualTo(stockAfter.getCount() - 1);
    }

    @Test
    public void sellTooManyStocks() throws URISyntaxException, IOException, InterruptedException {
        int testId = 10;
        clientDAO.createClient(testId, 0);
        Client client = clientDAO.getClient(testId);
        Stock stock = clientDAO.getStock(TEST_COMPANY_NAME);
        client.addStockInfo(stock);
        assertThatIllegalArgumentException().isThrownBy(() -> client.sellStock(TEST_COMPANY_NAME, 1));
    }

    @Test
    public void buyTooManyStocks() throws URISyntaxException, IOException, InterruptedException {
        int testId = 10;
        clientDAO.createClient(testId, 0);
        Client client = clientDAO.getClient(testId);
        Stock stock = clientDAO.getStock(TEST_COMPANY_NAME);
        client.addStockInfo(stock);
        assertThatIllegalArgumentException().isThrownBy(() -> client.buyStock(TEST_COMPANY_NAME, 1));
    }


    @After
    public void tearDown() {
        marketContainer.stop();

    }

}
