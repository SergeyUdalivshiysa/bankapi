package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface AccountService {

    void handleUpdateAmount(HttpExchange exchange);

    void handleFindAll(HttpExchange exchange);
}
