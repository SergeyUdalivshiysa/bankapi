package ru.volegov.controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.operator.AbstractControllerWithAuthorization;
import ru.volegov.controller.operator.OperatorPaymentController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.PaymentService;
import ru.volegov.model.service.implementation.PaymentServiceImpl;

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
