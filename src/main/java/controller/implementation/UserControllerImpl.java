package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.UserController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.UserService;
import model.service.implementation.UserServiceImpl;

@Controller(path = "/users")
public class UserControllerImpl extends AbstractControllerImpl implements UserController {

    private final UserService USER_SERVICE = new UserServiceImpl();

    @RequestMapping(path = "/{id}/counterparties", requestMethod = "GET")
    public void findAllCounterpartiesByUser(HttpExchange exchange, String id) {
        USER_SERVICE.handleFindAllCounterpartiesByUserId(exchange, id);
    }


    @RequestMapping(path = "/{id}/counterparties", requestMethod = "POST")
    public void createCounterpartyForUser(HttpExchange exchange, String id) {
        USER_SERVICE.handleCreate(exchange, id);
    }

}
