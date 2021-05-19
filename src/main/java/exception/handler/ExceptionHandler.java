package exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.util.ExceptionInfo;
import model.service.utils.ResponseHandler;

public class ExceptionHandler {

    private static final ObjectMapper OM = new ObjectMapper();
    private static final ResponseHandler RH = new ResponseHandler();

    public static void handleException(HttpExchange exchange, Exception exception) {
        try {
            String json = OM.writeValueAsString(new ExceptionInfo(exception.getMessage()));
            RH.handleResponseWithJsonBody(exchange, json, 400);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
