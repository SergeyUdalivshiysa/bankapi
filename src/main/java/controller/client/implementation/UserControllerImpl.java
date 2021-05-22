package controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.AbstractController;
import controller.client.UserController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.UserService;
import model.service.implementation.UserServiceImpl;

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
