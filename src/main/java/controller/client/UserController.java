package controller.client;

import com.sun.net.httpserver.HttpExchange;

public interface UserController {

     /**
      * Handles request on finding all counterparties of a particular user
      *
      * @param exchange
      * @param id
      */
     void findAllCounterpartiesByUser(HttpExchange exchange, String id);

     /**
      * Handles request on marking a certain user as counterparty of another one
      *
      * @param exchange
      */
     void addCounterparty(HttpExchange exchange);
}
