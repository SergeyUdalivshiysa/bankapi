package controller;

import com.sun.net.httpserver.HttpExchange;

public interface AccountController {

      void handleCardRequest(HttpExchange exchange) throws Exception;
}
