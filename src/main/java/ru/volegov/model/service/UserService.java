package ru.volegov.model.service;

import com.sun.net.httpserver.HttpExchange;

public interface UserService {
    void handleFindCounterpartiesByUserId(HttpExchange exchange, String id);

    void handleAddCounterparty(HttpExchange exchange);

    void handleCreateUser(HttpExchange exchange);
}
