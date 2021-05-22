package controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.operator.AbstractControllerWithAuthorization;
import controller.operator.OperatorPaymentController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.PaymentService;
import model.service.implementation.PaymentServiceImpl;

@Controller(path = "/operator/payments")
public class OperatorPaymentControllerImpl extends AbstractControllerWithAuthorization implements OperatorPaymentController {
    PaymentService paymentService;

    public OperatorPaymentControllerImpl() {
        paymentService = new PaymentServiceImpl();
    }

    @Override
    @RequestMapping(path = "/approve/{id}", requestMethod = "PUT")
    public void approvePayment(HttpExchange exchange, String id) {
        paymentService.handleApprovePayment(exchange, id);
    }

    @Override
    @RequestMapping(path = "/unapproved", requestMethod = "GET")
    public void getUnapprovedPayments(HttpExchange exchange) {
        paymentService.handleGetUnapprovedPayments(exchange);
    }
}
