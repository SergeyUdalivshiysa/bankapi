package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface UserService {
    void handleFindAllCounterpartiesByUserId(HttpExchange exchange, String id);

     void handleCreate(HttpExchange exchange, String id);
}
