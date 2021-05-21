package model.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.IncorrectInputDataException;
import exception.handler.ExceptionHandler;
import model.dto.PaymentDTO;
import model.repository.PaymentRepository;
import model.repository.implementation.PaymentRepositoryImpl;
import model.service.PaymentService;
import model.service.utils.ResponseHandler;

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
            paymentRepository.approvePayment(id);
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
