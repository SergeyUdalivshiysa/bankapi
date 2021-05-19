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

@Controller(path = "/accounts")
public class AccountControllerImpl extends AbstractControllerImpl implements AccountController {

    private  final AccountService ACCOUNT_SERVICE = new AccountServiceImpl();

    //Test
  /*  @RequestMapping(path = "", requestMethod = "get")
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
    }*/

    @Override
    @RequestMapping(path = "/", requestMethod = "PUT")
    public void updateAmount(HttpExchange exchange) {
        ACCOUNT_SERVICE.handleUpdateAmount(exchange);
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "GET")
    public void getAll(HttpExchange exchange) {
        ACCOUNT_SERVICE.handleFindAll(exchange);
    }


    @Override
    @RequestMapping(path = "/{id}/balance", requestMethod = "GET")
    public void getBalance(HttpExchange exchange, String id){
        ACCOUNT_SERVICE.handleGetBalance(exchange, id);
    }



}
