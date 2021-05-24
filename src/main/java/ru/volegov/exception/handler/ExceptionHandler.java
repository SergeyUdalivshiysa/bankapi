package ru.volegov.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.volegov.exception.IncorrectInputDataException;
import ru.volegov.exception.NotFoundException;
import ru.volegov.exception.util.ExceptionInfo;
import ru.volegov.model.service.utils.ResponseHandler;

public class ExceptionHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ResponseHandler HANDLER = new ResponseHandler();

    public static void handleException(HttpExchange exchange, Exception exception) {
        try {
            String json = getJson(exception);
            int code = getCode(exception);
            HANDLER.handleResponseWithJsonBody(exchange, json, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJson(Exception e) throws JsonProcessingException {
        if (e instanceof IncorrectInputDataException | e instanceof NotFoundException)
            return MAPPER.writeValueAsString(new ExceptionInfo(e.getMessage()));
        if (e instanceof JdbcSQLIntegrityConstraintViolationException | e instanceof JsonParseException)
            return MAPPER.writeValueAsString(new ExceptionInfo("Incorrect input data"));
        return MAPPER.writeValueAsString(new ExceptionInfo("Exception: " + e.getClass().getName()));
    }

    private static int getCode(Exception e) {
        if (e instanceof NotFoundException) return 404;
        return 400;
    }
}
