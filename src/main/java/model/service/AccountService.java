package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface AccountService {

    void handleUpdateAmount(HttpExchange exchange);

    void handleGetBalance(HttpExchange exchange, String id);

    void handleAddAccount(HttpExchange exchange);
}
