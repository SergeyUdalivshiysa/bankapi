package controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import controller.AccountController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.entities.Account;
import model.entities.User;
import model.service.AccountService;
import model.service.implementation.AccountServiceImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller(path = "/account")
public class AccountControllerImpl extends AbstractControllerImpl implements AccountController {

    private  final AccountService ACCOUNT_SERVICE = new AccountServiceImpl();

    @RequestMapping(path = "", requestMethod = "get")
    public void handleCardRequest(HttpExchange exchange) throws IOException {
        exchange.getRequestMethod();
        User user = new User(1, exchange.getRequestURI().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String respText = objectMapper.writeValueAsString(user);
        exchange.getResponseHeaders().add("content-type", "application/json");
        exchange.sendResponseHeaders(200, respText.length());
        OutputStream output = exchange.getResponseBody();
        output.write(respText.getBytes());
        output.flush();
        exchange.close();
    }

    @RequestMapping(path = "/", requestMethod = "PUT")
    public void updateAmount(HttpExchange exchange) {
        ACCOUNT_SERVICE.handleUpdateAmount(exchange);
    }

    @RequestMapping(path = "/", requestMethod = "GET")
    public void getAll(HttpExchange exchange) {
        ACCOUNT_SERVICE.handleFindAll(exchange);
    }






}
