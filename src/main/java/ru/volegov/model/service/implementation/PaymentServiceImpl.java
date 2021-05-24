package ru.volegov.model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ru.volegov.exception.IncorrectInputDataException;
import ru.volegov.exception.handler.ExceptionHandler;
import ru.volegov.model.dto.PaymentDTO;
import ru.volegov.model.repository.PaymentRepository;
import ru.volegov.model.repository.implementation.PaymentRepositoryImpl;
import ru.volegov.model.service.PaymentService;
import ru.volegov.model.service.utils.ResponseHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final ObjectMapper mapper;
    private final ResponseHandler responseHandler;

    public PaymentServiceImpl() {
        paymentRepository = new PaymentRepositoryImpl();
        mapper = new ObjectMapper();
        responseHandler = new ResponseHandler();
    }

    public void handleCreatePayment(HttpExchange exchange) {
        try {
            PaymentDTO paymentDTO = mapper.readValue(exchange.getRequestBody(), PaymentDTO.class);
            if (paymentDTO.getAmount().compareTo(new BigDecimal("0.00")) <= 0)
                throw new IncorrectInputDataException("Incorrect input data");
            paymentRepository.addPayment(paymentDTO);
            responseHandler.handleSuccessfulResponse(exchange, 201);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleApprovePayment(HttpExchange exchange, String id) {
        try {
            int paymentId = Integer.parseInt(id);
            paymentRepository.approvePayment(paymentId);
            responseHandler.handleSuccessfulResponse(exchange, 200);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }

    @Override
    public void handleGetUnapprovedPayments(HttpExchange exchange) {
        try {
            String json = mapper.writeValueAsString(paymentRepository.getUnapprovedPayments());
            responseHandler.handleResponseWithJsonBody(exchange, json, 200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ExceptionHandler.handleException(exchange, e);
        }
    }
}
