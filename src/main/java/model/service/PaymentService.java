package model.service;

import com.sun.net.httpserver.HttpExchange;

public interface PaymentService {

    void handleCreatePayment(HttpExchange exchange);

    void handleGetUnapprovedPayments(HttpExchange exchange);

    void handleApprovePayment(HttpExchange exchange, String id);
}
