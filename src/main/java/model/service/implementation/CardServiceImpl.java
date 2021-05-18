package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import model.entities.Card;
import model.repository.CardRepository;
import model.repository.implementation.CardRepositoryImpl;
import model.service.CardService;
import java.io.IOException;
import java.io.OutputStream;

public class CardServiceImpl implements CardService {

    private final CardRepository CARD_REPO = new CardRepositoryImpl();
    private final ObjectMapper OM = new ObjectMapper();

    public void handleFindAll(HttpExchange exchange) {
        try {
            String respText = OM.writeValueAsString(CARD_REPO.getAllCards());
            exchange.getResponseHeaders().add("content-type", "application/json");
            exchange.sendResponseHeaders(200, respText.length());
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCreateCard(HttpExchange exchange) {
        try {
            Card card = OM.readValue(exchange.getRequestBody(), Card.class);
            CARD_REPO.postCard(card);
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
