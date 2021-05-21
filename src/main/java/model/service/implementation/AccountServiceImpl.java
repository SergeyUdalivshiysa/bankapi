package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.IncorrectInputDataException;
import exception.handler.ExceptionHandler;
import model.dto.AccountDTO;
import model.dto.AccountMoneyDTO;
import model.repository.AccountRepository;
import model.repository.implementation.AccountRepositoryImpl;
import model.service.AccountService;
import model.service.utils.ResponseHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;


public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ResponseHandler responseHandler = new ResponseHandler();

    @Override
    public void handleUpdateAmount(HttpExchange exchange) {
        try {
            AccountMoneyDTO dto = mapper.readValue(exchange.getRequestBody(), AccountMoneyDTO.class);
            if (dto.getAmount().compareTo(new BigDecimal("0.00")) <= 0)
                throw new IncorrectInputDataException("Incorrect input data");
            accountRepository.update(dto);
            responseHandler.handleSuccessfulResponse(exchange, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleGetBalance(HttpExchange exchange, String id) {
        try {
            String respText = mapper.writeValueAsString(accountRepository.getBalance(id));
            responseHandler.handleResponseWithJsonBody(exchange, respText, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleAddAccount(HttpExchange exchange) {
        try {
            AccountDTO accountDTO = mapper.readValue(exchange.getRequestBody(), AccountDTO.class);
            accountRepository.addAccount(accountDTO);
            responseHandler.handleSuccessfulResponse(exchange, 201);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }
}
