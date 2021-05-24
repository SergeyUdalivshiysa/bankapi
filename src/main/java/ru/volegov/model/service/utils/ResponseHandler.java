package ru.volegov.model.service.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public void handleResponseWithJsonBody(HttpExchange exchange, String json, int code) throws IOException {
        exchange.getResponseHeaders().add("content-type", "application/json");
        exchange.sendResponseHeaders(code, json.length());
        OutputStream output = exchange.getResponseBody();
        output.write(json.getBytes());
        output.flush();
        exchange.close();
    }

    public void handleSuccessfulResponse(HttpExchange exchange, int code) throws IOException {
        String json = "{\"success\": \"true\"}";
        exchange.getResponseHeaders().add("content-type", "application/json");
        exchange.sendResponseHeaders(code, json.length());
        OutputStream output = exchange.getResponseBody();
        output.write(json.getBytes());
        output.flush();
        exchange.close();
    }
}
