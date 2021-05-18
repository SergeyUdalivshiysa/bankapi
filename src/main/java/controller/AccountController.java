package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import framework.annotations.RequestMapping;
import model.entities.User;

import java.io.IOException;
import java.io.OutputStream;

public interface AccountController {


      void handleCardRequest(HttpExchange exchange) throws Exception;
}
