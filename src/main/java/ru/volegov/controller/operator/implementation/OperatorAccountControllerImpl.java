package ru.volegov.controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.operator.AbstractControllerWithAuthorization;
import ru.volegov.controller.operator.OperatorAccountController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.AccountService;
import ru.volegov.model.service.implementation.AccountServiceImpl;

@Controller(path = "/operator/accounts")
public class OperatorAccountControllerImpl extends AbstractControllerWithAuthorization implements OperatorAccountController {

    AccountService accountService;

    public OperatorAccountControllerImpl() {
        accountService = new AccountServiceImpl();
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void addAccount(HttpExchange exchange) {
        accountService.handleAddAccount(exchange);
    }
}
