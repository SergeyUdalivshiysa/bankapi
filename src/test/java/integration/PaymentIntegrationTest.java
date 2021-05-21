package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.entities.Payment;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedPayment = "select sender_id from payment order by id desc limit 1";
    private final String getMoneyAmountSql = "select amount from account where id = ?";


    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void createPayment() {
        try {
            String amount = "100.00";
            int senderId = 1;
            int receiverId = 2;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("amount", amount);
            objectNode.put("senderId", senderId);
            objectNode.put("receiverId", receiverId);
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/payments");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedPayment);
            ResultSet resultSet = preparedStatement.executeQuery();
            int actual = 0;
            if (resultSet.next()) actual = resultSet.getInt(1);
            preparedStatement.close();
            connection.close();
            assertEquals(senderId, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void approvePayment() {
        try {
            BigDecimal amount = BigDecimal.valueOf(1.00);
            int senderId = 2;
            int receiver_id = 1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut("http://localhost:8080/payments/approve/1");
            CloseableHttpResponse response = httpClient.execute(httpPut);
            assertEquals(200, response.getStatusLine().getStatusCode());

            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement("");
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
    void getUnapprovedPayments() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/payments/unapproved");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            List<Payment> payments = new ArrayList<>();
            Payment payment = new Payment(1, new BigDecimal("1.00"), false, 2, 1);
            payments.add(payment);
            String expected = objectMapper.writeValueAsString(payments);
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
}