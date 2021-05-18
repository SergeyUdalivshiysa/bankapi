package exception.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ExceptionHandler {
    public static void handleException(HttpExchange exchange, Exception exception) throws IOException {
        String json = getJsonString(exception);
        exchange.getResponseHeaders().add("content-type", "application/json");
        exchange.sendResponseHeaders(400, json.length());
        OutputStream output = exchange.getResponseBody();
        output.write(json.getBytes());
        output.flush();
        exchange.close();
    }

    public static String getJsonString(Exception e){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"info\": ").append("\"").append(e.getMessage()).append("\" }");
        return stringBuilder.toString();
    }
}
