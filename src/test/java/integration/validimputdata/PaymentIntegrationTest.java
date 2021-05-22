package integration.validimputdata;

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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedPayment = "select id, amount, approved, sender_id, receiver_id from payment order by id desc limit 1";
    private final String getMoneyAmountSql = "select amount from account where id = ?";
    private final String getApprovedFlag = "select approved from payment where id = ?";


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
            Payment actualPayment = null;
            if (resultSet.next()) actualPayment = buildPayment(resultSet);
            preparedStatement.close();
            connection.close();
            Payment expectedPayment = new Payment(3,
                    BigDecimal.valueOf(100).setScale(2),
                    false,
                    1,
                    2);
            assertEquals(expectedPayment, actualPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Payment buildPayment(ResultSet resultSet) throws SQLException {
        return new Payment(
                resultSet.getInt(1),
                resultSet.getBigDecimal(2),
                resultSet.getBoolean(3),
                resultSet.getInt(4),
                resultSet.getInt(5)
        );
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
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void approvePayment() {
        try {
            int paymentId = 1;
            BigDecimal amount = BigDecimal.valueOf(1.00);
            int senderId = 2;
            int receiver_id = 1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut("http://localhost:8080/payments/approve/1");
            CloseableHttpResponse response = httpClient.execute(httpPut);
            assertEquals(200, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            BigDecimal senderMoneyActual = getActualSenderMoney(connection, senderId);
            BigDecimal receiverMoneyActual = getActualReceiverMoney(connection, receiver_id);
            boolean isApprovedActual = getIsApproved(connection, paymentId);
            assertEquals(new BigDecimal("1.00"), senderMoneyActual);
            assertEquals(new BigDecimal("201.50"), receiverMoneyActual);
            assertTrue(isApprovedActual);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getActualSenderMoney(Connection connection, int id) throws SQLException {
        PreparedStatement senderMoneyStatement = connection.prepareStatement(getMoneyAmountSql);
        senderMoneyStatement.setInt(1, id);
        ResultSet senderResultSet = senderMoneyStatement.executeQuery();
        BigDecimal senderMoney = null;
        if (senderResultSet.next()) {
            senderMoney = senderResultSet.getBigDecimal(1);
        }
        senderMoneyStatement.close();
        return senderMoney;
    }

    private BigDecimal getActualReceiverMoney(Connection connection, int id) throws SQLException {
        PreparedStatement receiverMoneyStatement = connection.prepareStatement(getMoneyAmountSql);
        receiverMoneyStatement.setInt(1, id);
        ResultSet receiverResultSet = receiverMoneyStatement.executeQuery();
        BigDecimal receiverMoney = null;
        if (receiverResultSet.next()) {
            receiverMoney = receiverResultSet.getBigDecimal(1);
        }
        receiverMoneyStatement.close();
        return receiverMoney;
    }

    private boolean getIsApproved(Connection connection, int id) throws SQLException {
        PreparedStatement isApprovedStatement = connection.prepareStatement(getApprovedFlag);
        isApprovedStatement.setInt(1, id);
        ResultSet isApprovedResultSet = isApprovedStatement.executeQuery();
        boolean isApprovedActual = false;
        if (isApprovedResultSet.next()) {
            isApprovedActual = isApprovedResultSet.getBoolean(1);
        }
        isApprovedStatement.close();
        return isApprovedActual;
    }
}