package controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.AbstractController;
import controller.client.PaymentController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.PaymentService;
import model.service.implementation.PaymentServiceImpl;

@Controller(path = "/payments")
public class PaymentControllerImpl extends AbstractController implements PaymentController {

    private final PaymentService paymentService;

    public PaymentControllerImpl() {
        paymentService = new PaymentServiceImpl();
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void createPayment(HttpExchange exchange) {
        paymentService.handleCreatePayment(exchange);
    }
}
