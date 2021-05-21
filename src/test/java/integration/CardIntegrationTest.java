package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.entities.Card;
import model.repository.util.DataBaseFiller;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PropertiesManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedCard = "select account_id from card order by id desc limit 1";
    private final String getIsApproved = "select active from card where id = ?";

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void findAll() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/cards");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Card card1 = new Card(1, "1000000000000000", 1, true);
            Card card2 = new Card(2, "1000000000000001", 1, false);
            List<Card> cardList = new ArrayList<>();
            cardList.add(card1);
            cardList.add(card2);
            String expected = objectMapper.writeValueAsString(cardList);
            String actual = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent(),
                            StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            ;
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void createCard() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("accountId", "2");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/cards");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedCard);
            ResultSet resultSet = preparedStatement.executeQuery();
            int actual = 0;
            if (resultSet.next()) actual = resultSet.getInt(1);
            preparedStatement.close();
            connection.close();
            assertEquals(2, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void findUnapproved() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/cards/unapproved");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Card card1 = new Card(2, "1000000000000001", 1, false);
            List<Card> cardList = new ArrayList<>();
            cardList.add(card1);
            String expected = objectMapper.writeValueAsString(cardList);
            String actual = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent(),
                            StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            ;
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void activateCard() {
        try {
            int id = 2;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut("http://localhost:8080/cards/approve/" + id);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getIsApproved);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean actual = false;
            if (resultSet.next()) actual = resultSet.getBoolean(1);
            preparedStatement.close();
            connection.close();
            assertEquals(true, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}