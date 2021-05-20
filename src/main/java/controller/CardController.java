package controller;

import com.sun.net.httpserver.HttpExchange;

public interface CardController {

    void findAll(HttpExchange exchange);

    void getCardById(HttpExchange exchange, String id);

    void createCard(HttpExchange exchange);

    void findUnapproved(HttpExchange exchange);

    void activateCard(HttpExchange exchange, String id);
}
