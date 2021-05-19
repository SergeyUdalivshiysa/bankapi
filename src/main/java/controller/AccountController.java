package controller;

import com.sun.net.httpserver.HttpExchange;
import framework.annotations.RequestMapping;

public interface AccountController {

      void updateAmount(HttpExchange exchange);

      void getAll(HttpExchange exchange);

      void getBalance(HttpExchange exchange, String id);
}