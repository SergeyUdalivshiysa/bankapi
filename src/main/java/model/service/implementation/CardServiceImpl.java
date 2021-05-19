package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.handler.ExceptionHandler;
import model.entities.Card;
import model.repository.CardRepository;
import model.repository.implementation.CardRepositoryImpl;
import model.service.CardService;
import model.service.utils.ResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class CardServiceImpl implements CardService {

    private final CardRepository CARD_REPO = new CardRepositoryImpl();
    private final ObjectMapper OM = new ObjectMapper();
    private final ResponseHandler RH = new ResponseHandler();

    public void handleFindAll(HttpExchange exchange) {
        try {
            String respText = OM.writeValueAsString(CARD_REPO.getAllCards());
            RH.handleResponseWithJsonBody(exchange, respText, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleCreateCard(HttpExchange exchange) {
        try {
            Card card = OM.readValue(exchange.getRequestBody(), Card.class);
            CARD_REPO.postCard(card);
            RH.handleResponseWithNoBody(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
