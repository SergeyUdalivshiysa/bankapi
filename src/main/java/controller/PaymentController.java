package controller;

import com.sun.net.httpserver.HttpExchange;

public interface PaymentController {

    void createPayment(HttpExchange exchange);

    void approvePayment(HttpExchange exchange, String id);

    void getUnapprovedPayments(HttpExchange exchange);
}
