package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.NoSuchAccountException;
import exception.handler.ExceptionHandler;
import model.repository.AccountRepository;
import model.repository.dto.AccountMoneyDTO;
import model.repository.implementation.AccountRepositoryImpl;
import model.service.AccountService;
import model.service.utils.ResponseHandler;
import java.io.IOException;
import java.sql.SQLException;


public class AccountServiceImpl implements AccountService {

    private final AccountRepository ACC_REPO = new AccountRepositoryImpl();
    private final ObjectMapper OM = new ObjectMapper();
    private final ResponseHandler RH = new ResponseHandler();

    @Override
    public void handleUpdateAmount(HttpExchange exchange) {
        try {
            AccountMoneyDTO dto = OM.readValue(exchange.getRequestBody(), AccountMoneyDTO.class);
            ACC_REPO.update(dto);
            RH.handleResponseWithNoBody(exchange, 200);
        } catch (IOException | NoSuchAccountException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleFindAll(HttpExchange exchange) {
        try {
            String respText = OM.writeValueAsString(ACC_REPO.getAllAccounts());
            RH.handleResponseWithJsonBody(exchange, respText, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleGetBalance(HttpExchange exchange, String id) {
            try {
                String respText = OM.writeValueAsString(ACC_REPO.getBalance(id));
                RH.handleResponseWithJsonBody(exchange, respText, 200);
            } catch (IOException | NoSuchAccountException | SQLException e) {
                e.printStackTrace();
                ExceptionHandler.handleException(exchange, e);
            }

    }
}
