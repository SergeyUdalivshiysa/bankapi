package ru.volegov.controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.AbstractController;
import ru.volegov.controller.client.PaymentController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.PaymentService;
import ru.volegov.model.service.implementation.PaymentServiceImpl;

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
