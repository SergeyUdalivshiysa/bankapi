package model.service;

import com.sun.net.httpserver.HttpExchange;

import java.sql.SQLException;

public interface AccountService {

    void handleUpdateAmount(HttpExchange exchange);

    void handleFindAll(HttpExchange exchange);

    void handleGetBalance(HttpExchange exchange, String id);
}
