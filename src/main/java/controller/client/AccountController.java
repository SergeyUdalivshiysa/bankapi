package controller.client;

import com.sun.net.httpserver.HttpExchange;

public interface AccountController {
      /**
       * Handles request on updating amount of money on the account
       *
       * @param exchange
       */
      void updateAmount(HttpExchange exchange);

      /**
       * Handles request on checking amount of money on the account
       *
       * @param exchange
       * @param id
       */
      void getBalance(HttpExchange exchange, String id);
}