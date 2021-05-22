package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.AccountService;
import model.service.implementation.AccountServiceImpl;

@Controller(path = "/accounts")
public class AccountControllerImpl extends AbstractController implements controller.AccountController {

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

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void addAccount(HttpExchange exchange) {
        accountService.handleAddAccount(exchange);
    }

}
