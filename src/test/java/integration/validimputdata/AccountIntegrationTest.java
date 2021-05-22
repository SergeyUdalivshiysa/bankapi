package integration.validimputdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.entities.Account;
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
import utils.HttpClientCreator;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountIntegrationTest {

    private final String getMoneyAmountSql = "select amount from account where id = ?";
    private final String getLastCreatedAccount = "select id, number, amount, user_id from account order by id desc limit 1";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void updateAmount() {
        try {
            int id = 1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("id", 1);
            objectNode.put("amount", "200");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPut httpPut = new HttpPut("http://localhost:8080/accounts");
            httpPut.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPut);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getMoneyAmountSql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal actual = null;
            if (resultSet.next()) actual = resultSet.getBigDecimal(1);
            preparedStatement.close();
            connection.close();
            httpClient.close();
            assertEquals(new BigDecimal("400.50"), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void getBalance() {
        try {
            int accountId = 1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/accounts/" + accountId + "/balance");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getMoneyAmountSql);
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal actual = null;
            if (resultSet.next()) actual = resultSet.getBigDecimal(1);
            preparedStatement.close();
            connection.close();
            httpClient.close();
            assertEquals(new BigDecimal("200.50"), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addAccount() {
        try {
            CloseableHttpClient httpClient = HttpClientCreator.getHttpClientWithAuthorization();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("userId", "1");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/operator/accounts");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedAccount);
            ResultSet resultSet = preparedStatement.executeQuery();
            Account actualAccount = null;
            if (resultSet.next()) actualAccount = buildAccount(resultSet);
            Account expectedAccount = new Account(3, "1000000000000002", BigDecimal.valueOf(0).setScale(2), 1);
            preparedStatement.close();
            connection.close();
            httpClient.close();
            response.close();
            assertEquals(expectedAccount, actualAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Account buildAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBigDecimal(3),
                resultSet.getInt(4));
    }

}