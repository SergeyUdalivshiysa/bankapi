package controller.operator;

import com.sun.net.httpserver.HttpExchange;

public interface OperatorPaymentController {
    /**
     * Handles request on approving payment
     *
     * @param exchange
     * @param id
     */
    void approvePayment(HttpExchange exchange, String id);

    /**
     * Handles request on finding all unapproved payments
     *
     * @param exchange
     */
    void getUnapprovedPayments(HttpExchange exchange);
}
