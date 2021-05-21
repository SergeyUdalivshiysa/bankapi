package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.UserService;
import model.service.implementation.UserServiceImpl;

@Controller(path = "/users")
public class UserAbstractController extends AbstractController implements controller.UserController {

    private final UserService userService;

    public UserAbstractController() {
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

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void createUser(HttpExchange exchange) {
        userService.handleCreateUser(exchange);
    }

}
