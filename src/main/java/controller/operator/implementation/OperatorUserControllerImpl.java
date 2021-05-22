package controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.operator.AbstractControllerWithAuthorization;
import controller.operator.OperatorUserController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.UserService;
import model.service.implementation.UserServiceImpl;

@Controller(path = "/operator/users")
public class OperatorUserControllerImpl extends AbstractControllerWithAuthorization implements OperatorUserController {

    UserService userService;

    public OperatorUserControllerImpl() {
        userService = new UserServiceImpl();
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void createUser(HttpExchange exchange) {
        userService.handleCreateUser(exchange);
    }
}
