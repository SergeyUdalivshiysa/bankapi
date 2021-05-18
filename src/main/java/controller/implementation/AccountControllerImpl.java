package controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import controller.AccountController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.entities.User;
import java.io.IOException;
import java.io.OutputStream;

@Controller(path = "/account")
public class AccountControllerImpl extends AbstractControllerImpl implements AccountController {

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






}
