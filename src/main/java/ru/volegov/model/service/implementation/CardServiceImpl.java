package ru.volegov.model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ru.volegov.exception.handler.ExceptionHandler;
import ru.volegov.model.dto.CardDTO;
import ru.volegov.model.entities.Card;
import ru.volegov.model.repository.CardRepository;
import ru.volegov.model.repository.implementation.CardRepositoryImpl;
import ru.volegov.model.service.CardService;
import ru.volegov.model.service.utils.ResponseHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ObjectMapper mapper;
    private final ResponseHandler responseHandler;

    public CardServiceImpl() {
        cardRepository = new CardRepositoryImpl();
        mapper = new ObjectMapper();
        responseHandler = new ResponseHandler();
    }

    public void handleFindCardsByAccountId(HttpExchange exchange, String id) {
        try {
            int accountId = Integer.parseInt(id);
            handleFindCards(exchange, cardRepository.getCardsByAccountId(accountId));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleCreateCard(HttpExchange exchange) {
        try {
            CardDTO dto = mapper.readValue(exchange.getRequestBody(), CardDTO.class);
            cardRepository.addCard(dto);
            responseHandler.handleSuccessfulResponse(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleFindUnapprovedCards(HttpExchange exchange) {
        try {
            handleFindCards(exchange, cardRepository.getUnapprovedCards());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleActivateCard(HttpExchange exchange, String id) {
        try {
            int accountId = Integer.parseInt(id);
            cardRepository.activateCard(accountId);
            responseHandler.handleSuccessfulResponse(exchange, 200);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    private void handleFindCards(HttpExchange exchange, List<Card> cards) throws IOException {
        String respText = mapper.writeValueAsString(cards);
        responseHandler.handleResponseWithJsonBody(exchange, respText, 200);
    }

}
