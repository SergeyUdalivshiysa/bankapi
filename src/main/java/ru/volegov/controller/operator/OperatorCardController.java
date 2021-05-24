package ru.volegov.controller.operator;

import com.sun.net.httpserver.HttpExchange;

public interface OperatorCardController {
    /**
     * Handles request on finding all unapproved cards
     *
     * @param exchange
     */
    void findUnapproved(HttpExchange exchange);

    /**
     * Handles request on activating card
     *
     * @param exchange
     * @param id
     */
    void activateCard(HttpExchange exchange, String id);

}
