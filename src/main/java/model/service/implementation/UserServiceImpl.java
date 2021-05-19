package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.handler.ExceptionHandler;
import model.entities.User;
import model.repository.UserRepository;
import model.repository.implementation.UserRepositoryImpl;
import model.service.UserService;
import model.service.utils.ResponseHandler;
import java.io.IOException;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private static final UserRepository USER_REPO = new UserRepositoryImpl();
    private static final ObjectMapper OM = new ObjectMapper();
    private final ResponseHandler RH = new ResponseHandler();

    public void handleFindAllCounterpartiesByUserId(HttpExchange exchange, String id) {
        try {
            String respText = OM.writeValueAsString(USER_REPO.findAllCounterparties(id));
            RH.handleResponseWithJsonBody(exchange, respText, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCreate(HttpExchange exchange, String id) {
        try {
            User user = OM.readValue(exchange.getRequestBody(), User.class);
            user.setCounterparty(true);
            user.setPartnerId(Integer.parseInt(id));
            USER_REPO.postUserAsCounterparty(user);
            RH.handleResponseWithNoBody(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }
}
