package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.handler.ExceptionHandler;
import model.entities.User;
import model.repository.UserRepository;
import model.repository.dto.CounterpartyDTO;
import model.repository.implementation.UserRepositoryImpl;
import model.service.UserService;
import model.service.utils.ResponseHandler;

import java.io.IOException;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ResponseHandler responseHandler = new ResponseHandler();

    public void handleFindCounterpartiesByUserId(HttpExchange exchange, String id) {
        try {
            String respText = mapper.writeValueAsString(userRepository.findCounterpartiesById(id));
            responseHandler.handleResponseWithJsonBody(exchange, respText, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    public void handleAddCounterparty(HttpExchange exchange) {
        try {
            CounterpartyDTO counterpartyDTO = mapper.readValue(exchange.getRequestBody(), CounterpartyDTO.class);
            userRepository.addCounterparty(counterpartyDTO);
            responseHandler.handleSuccessfulResponse(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleCreateUser(HttpExchange exchange) {
        try {
            User user = mapper.readValue(exchange.getRequestBody(), User.class);
            System.out.println(user);
            userRepository.addUser(user);
            responseHandler.handleSuccessfulResponse(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }


}
