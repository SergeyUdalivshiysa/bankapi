package ru.volegov.controller.client;

import com.sun.net.httpserver.HttpExchange;

public interface CardController {

    /**
     * Handles request on finding all cards of a certain account
     *
     * @param exchange
     * @param id
     */
    void findCardsByAccountId(HttpExchange exchange, String id);

    /**
     * Creates a card for an account
     *
     * @param exchange
     */
    void createCard(HttpExchange exchange);
}
