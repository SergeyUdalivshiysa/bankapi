package integration.invalidimputdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import utils.HttpClientCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserIntegrationInvalidInputDataTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void findCounterpartiesById() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/users/5/counterparties");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            response.close();
            httpClient.close();
            assertEquals(404, response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addCounterparty() {
        try {
            int partyId = 9;
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
            response.close();
            httpClient.close();
            assertEquals(400, response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void addUser() {
        try {
            String name = "Slava";
            boolean isLegalEntity = true;
            CloseableHttpClient httpClient = HttpClientCreator.getHttpClientWithAuthorization();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("name", name);
            objectNode.put("legalEntity", "yes");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/operator/users");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            response.close();
            httpClient.close();
            assertEquals(400, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}