package ru.volegov.controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.AbstractController;
import ru.volegov.controller.client.UserController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.UserService;
import ru.volegov.model.service.implementation.UserServiceImpl;

@Controller(path = "/users")
public class UserControllerImpl extends AbstractController implements UserController {

    private final UserService userService;

    public UserControllerImpl() {
        userService = new UserServiceImpl();
    }

    @Override
    @RequestMapping(path = "/{id}/counterparties", requestMethod = "GET")
    public void findAllCounterpartiesByUser(HttpExchange exchange, String id) {
        userService.handleFindCounterpartiesByUserId(exchange, id);
    }

    @Override
    @RequestMapping(path = "/counterparties", requestMethod = "POST")
    public void addCounterparty(HttpExchange exchange) {
        userService.handleAddCounterparty(exchange);
    }

}
