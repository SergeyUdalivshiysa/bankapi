package controller.operator;

import com.sun.net.httpserver.HttpExchange;

public interface OperatorUserController {
    /**
     * Handles request on creating a user
     *
     * @param exchange
     */
    void createUser(HttpExchange exchange);
}
