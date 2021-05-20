package controller;

import com.sun.net.httpserver.HttpExchange;

public interface UserController {

     void findAllCounterpartiesByUser(HttpExchange exchange, String id);

     void addCounterparty(HttpExchange exchange);

     void createUser(HttpExchange exchange);
}
