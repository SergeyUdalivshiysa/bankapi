package integration.invalidimputdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.repository.util.DataBaseFiller;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.HttpClientCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentIntegrationInvalidInputDataTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void createPayment() {
        try {
            String amount = "100.00";
            int senderId = 2;
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
            int code = response.getStatusLine().getStatusCode();
            response.close();
            httpClient.close();
            assertEquals(400, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void approvePayment() {
        try {
            CloseableHttpClient httpClient = HttpClientCreator.getHttpClientWithAuthorization();
            HttpPut httpPut = new HttpPut("http://localhost:8080/operator/payments/approve/2");
            CloseableHttpResponse response = httpClient.execute(httpPut);
            int code = response.getStatusLine().getStatusCode();
            response.close();
            httpClient.close();
            assertEquals(400, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}