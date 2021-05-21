package controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountControllerImplTest {

    private final String getMoneyAmountSql = "select amount from account where id = 1";
    private final String getLastCreatedAccount = "select user_id from account order by id desc limit 1";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void updateAmount() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("id", "1");
            objectNode.put("amount", "200");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPut httpPut = new HttpPut("http://localhost:8080/accounts");
            httpPut.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPut);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getMoneyAmountSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal actual = null;
            if (resultSet.next()) actual = resultSet.getBigDecimal(1);
            preparedStatement.close();
            connection.close();
            assertEquals(new BigDecimal("400.50"), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void getBalance() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/accounts/1/balance");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getMoneyAmountSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal actual = null;
            if (resultSet.next()) actual = resultSet.getBigDecimal(1);
            preparedStatement.close();
            connection.close();
            assertEquals(new BigDecimal("200.50"), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addAccount() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("userId", "1");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/accounts");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedAccount);
            ResultSet resultSet = preparedStatement.executeQuery();
            int actual = 0;
            if (resultSet.next()) actual = resultSet.getInt(1);
            preparedStatement.close();
            connection.close();
            assertEquals(1, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}