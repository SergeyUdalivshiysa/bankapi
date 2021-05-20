package exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.IncorrectInputDataException;
import exception.util.ExceptionInfo;
import model.service.utils.ResponseHandler;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

public class ExceptionHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ResponseHandler HANDLER = new ResponseHandler();

    public static void handleException(HttpExchange exchange, Exception exception) {
        try {
            String json = getJson(exception);
            HANDLER.handleResponseWithJsonBody(exchange, json, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJson(Exception e) throws JsonProcessingException {
        if (e instanceof IncorrectInputDataException)
            return MAPPER.writeValueAsString(new ExceptionInfo(e.getMessage()));
        if (e instanceof JdbcSQLIntegrityConstraintViolationException | e instanceof JsonParseException)
            return MAPPER.writeValueAsString(new ExceptionInfo("Incorrect input data"));
        return MAPPER.writeValueAsString(new ExceptionInfo("Exception: " + e.getClass().getName()));
    }
}
