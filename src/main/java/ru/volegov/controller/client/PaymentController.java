package ru.volegov.controller.client;

import com.sun.net.httpserver.HttpExchange;

public interface PaymentController {

    /**
     * Handles request on creating an instance of a payment.
     *
     * @param exchange
     */
    void createPayment(HttpExchange exchange);
}
