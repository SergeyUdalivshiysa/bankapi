package integration.invalidimputdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.repository.util.DataBaseFiller;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.HttpClientCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardIntegrationInvalidInputDataTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void getCardsByAccountId() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/cards/9");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            assertEquals(404, response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void createCard() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("accountId", "4");
            String json = objectMapper.writeValueAsString(objectNode);
            HttpPost httpPost = new HttpPost("http://localhost:8080/cards");
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
    void findUnapproved() {
        try {
            CloseableHttpClient httpClient = HttpClientCreator.getHttpClientWithAuthorization();
            HttpGet httpGet = new HttpGet("http://localhost:8080/operator/cards/approved");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            response.close();
            httpClient.close();
            assertEquals(404, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void activateCard() {
        try {
            int id = 1;
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials("admin", "dmin");
            provider.setCredentials(AuthScope.ANY, credentials);
            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
            HttpPut httpPut = new HttpPut("http://localhost:8080/operator/cards/approve/" + id);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            int code = response.getStatusLine().getStatusCode();
            response.close();
            httpClient.close();
            assertEquals(401, code);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}