package controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.entities.User;
import model.repository.util.DataBaseFiller;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

class UserControllerImplTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedUserSql = "select name, legal_entity from user order by id desc limit 1";
    private final String getCounterpartySql = "select id from user u join counterparty c on u.id = c.counterparty_id where party_id = ?";

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void findCounterpartiesById() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/users/1/counterparties");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(200, response.getStatusLine().getStatusCode());
            List<User> users = new ArrayList<>();
            User user = new User(2, "Vlad", false);
            users.add(user);
            String expected = objectMapper.writeValueAsString(users);
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
    void addCounterparty() {
        try {
            int partyId = 3;
            int counterpartyId = 2;
            String name = "Slava";
            boolean isLegalEntity = true;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("partyId", partyId);
            objectNode.put("counterpartyId", counterpartyId);
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/users/counterparties");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());

            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getCounterpartySql);
            preparedStatement.setInt(1, partyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            String actualName = "";
            int actual = 0;
            if (resultSet.next()) {
                actual = resultSet.getInt(1);

            }
            assertEquals(2, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void addUser() {
        try {
            String name = "Slava";
            boolean isLegalEntity = true;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("name", name);
            objectNode.put("legalEntity", isLegalEntity);
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/users");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            assertEquals(201, response.getStatusLine().getStatusCode());
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedUserSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            String actualName = "";
            boolean actualIsLegalEntity = false;
            if (resultSet.next()) {
                actualName = resultSet.getString(1);
                actualIsLegalEntity = resultSet.getBoolean(2);
            }
            preparedStatement.close();
            connection.close();
            assertEquals(name, actualName);
            assertEquals(isLegalEntity, actualIsLegalEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}