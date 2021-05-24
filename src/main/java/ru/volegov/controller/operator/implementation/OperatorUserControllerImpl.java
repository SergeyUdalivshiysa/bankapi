package ru.volegov.controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.operator.AbstractControllerWithAuthorization;
import ru.volegov.controller.operator.OperatorUserController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.UserService;
import ru.volegov.model.service.implementation.UserServiceImpl;

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
