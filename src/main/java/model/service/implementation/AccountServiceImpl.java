package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.NoSuchAccountException;
import exception.util.ExceptionHandler;
import exception.util.ExceptionMessageGetter;
import model.repository.AccountRepository;
import model.repository.dto.AccountMoneyDTO;
import model.repository.implementation.AccountRepositoryImpl;
import model.service.AccountService;
import java.io.IOException;
import java.io.OutputStream;


public class AccountServiceImpl implements AccountService {

    private final AccountRepository ACC_REPO = new AccountRepositoryImpl();
    private final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handleUpdateAmount(HttpExchange exchange) {
        try {
            AccountMoneyDTO dto = OM.readValue(exchange.getRequestBody(), AccountMoneyDTO.class);
            ACC_REPO.update(dto);
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        } catch (IOException | NoSuchAccountException e) {
            e.printStackTrace();
            try {
                ExceptionHandler.handleException(exchange, e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void handleFindAll(HttpExchange exchange) {
        try {
            String respText = OM.writeValueAsString(ACC_REPO.getAllAccounts());
            exchange.getResponseHeaders().add("content-type", "application/json");
            exchange.sendResponseHeaders(200, respText.length());
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
