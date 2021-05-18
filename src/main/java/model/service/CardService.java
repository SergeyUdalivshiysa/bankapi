package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface CardService {
    void handleFindAll(HttpExchange exchange);

    void handleCreateCard(HttpExchange exchange);
}
