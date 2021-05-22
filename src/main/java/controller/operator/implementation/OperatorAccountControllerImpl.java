package controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.operator.AbstractControllerWithAuthorization;
import controller.operator.OperatorAccountController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.AccountService;
import model.service.implementation.AccountServiceImpl;

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
