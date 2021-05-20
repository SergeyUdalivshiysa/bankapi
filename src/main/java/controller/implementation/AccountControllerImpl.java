package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.AccountController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.AccountService;
import model.service.implementation.AccountServiceImpl;

@Controller(path = "/accounts")
public class AccountControllerImpl extends AbstractControllerImpl implements AccountController {

    private final AccountService accountService = new AccountServiceImpl();

    @Override
    @RequestMapping(path = "/", requestMethod = "PUT")
    public void updateAmount(HttpExchange exchange) {
        accountService.handleUpdateAmount(exchange);
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "GET")
    public void getAll(HttpExchange exchange) {
        accountService.handleFindAll(exchange);
    }


    @Override
    @RequestMapping(path = "/{id}/balance", requestMethod = "GET")
    public void getBalance(HttpExchange exchange, String id) {
        accountService.handleGetBalance(exchange, id);
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void putAccount(HttpExchange exchange) {
        accountService.handleAddAccount(exchange);
    }

}
