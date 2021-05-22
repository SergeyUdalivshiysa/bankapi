package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface CardService {
    void handleFindCardsByAccountId(HttpExchange exchange, String id);

    void handleCreateCard(HttpExchange exchange);

    void handleFindUnapprovedCards(HttpExchange exchange);

    void handleActivateCard(HttpExchange exchange, String id);
}
