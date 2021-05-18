package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.entities.Card;
import model.repository.CardRepository;
import java.io.IOException;
import java.io.OutputStream;

@Controller(path = "/card")
public class CardController extends AbstractController {

    @RequestMapping(path = "", requestMethod = "GET")
    public void findAll(HttpExchange exchange) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String respText = objectMapper.writeValueAsString(CardRepository.getAllCards());
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

    @RequestMapping(path = "/{id}", requestMethod = "GET")
    public void getCardById(HttpExchange exchange, String id) {
        System.out.println(id);
    }

    @RequestMapping(path = "", requestMethod = "POST")
    public void createCard(HttpExchange exchange) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Card card = objectMapper.readValue(exchange.getRequestBody(), Card.class);
            System.out.println(card);
            CardRepository.postCard(card);
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
