package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.PaymentController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.PaymentService;
import model.service.implementation.PaymentServiceImpl;

@Controller(path = "/payments")
public class PaymentControllerImpl extends AbstractControllerImpl implements PaymentController {

    private final PaymentService paymentService = new PaymentServiceImpl();

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void createPayment(HttpExchange exchange) {
        paymentService.handleCreatePayment(exchange);
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