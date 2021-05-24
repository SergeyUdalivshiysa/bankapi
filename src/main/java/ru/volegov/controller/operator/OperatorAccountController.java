package ru.volegov.controller.operator;

import com.sun.net.httpserver.HttpExchange;

public interface OperatorAccountController {
    /**
     * Handles request on creating an account for a user
     *
     * @param exchange
     */
    void addAccount(HttpExchange exchange);
}
