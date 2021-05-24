package ru.volegov.controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.AbstractController;
import ru.volegov.controller.client.AccountController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.AccountService;
import ru.volegov.model.service.implementation.AccountServiceImpl;

@Controller(path = "/accounts")
public class AccountControllerImpl extends AbstractController implements AccountController {

    private final AccountService accountService;

    public AccountControllerImpl() {
        accountService = new AccountServiceImpl();
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "PUT")
    public void updateAmount(HttpExchange exchange) {
        accountService.handleUpdateAmount(exchange);
    }

    @Override
    @RequestMapping(path = "/{id}/balance", requestMethod = "GET")
    public void getBalance(HttpExchange exchange, String id) {
        accountService.handleGetBalance(exchange, id);
    }

}
