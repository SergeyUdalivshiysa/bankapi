package ru.volegov.model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ru.volegov.exception.IncorrectInputDataException;
import ru.volegov.exception.handler.ExceptionHandler;
import ru.volegov.model.dto.AccountDTO;
import ru.volegov.model.dto.AccountMoneyDTO;
import ru.volegov.model.repository.AccountRepository;
import ru.volegov.model.repository.implementation.AccountRepositoryImpl;
import ru.volegov.model.service.AccountService;
import ru.volegov.model.service.utils.ResponseHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;


public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ObjectMapper mapper;
    private final ResponseHandler responseHandler;

    public AccountServiceImpl() {
        accountRepository = new AccountRepositoryImpl();
        mapper = new ObjectMapper();
        responseHandler = new ResponseHandler();
    }

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
            int accountId = Integer.parseInt(id);
            String respText = mapper.writeValueAsString(accountRepository.getBalance(accountId));
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
