package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.CardController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.CardService;
import model.service.implementation.CardServiceImpl;

@Controller(path = "/cards")
public class CardControllerImplImpl extends AbstractControllerImpl implements CardController {

    private final CardService cardService = new CardServiceImpl();

    @Override
    @RequestMapping(path = "/", requestMethod = "GET")
    public void findAll(HttpExchange exchange) {
        cardService.handleFindAll(exchange);
    }

    @Override
    @RequestMapping(path = "/test/{id}", requestMethod = "GET")
    public void getCardById(HttpExchange exchange, String id) {
        System.out.println(id);
    }

    @Override
    @RequestMapping(path = "/", requestMethod = "POST")
    public void createCard(HttpExchange exchange) {
        cardService.handleCreateCard(exchange);
    }

    @Override
    @RequestMapping(path = "/unapproved", requestMethod = "GET")
    public void findUnapproved(HttpExchange exchange) {
        cardService.handleFindUnapprovedCards(exchange);
    }

    @Override
    @RequestMapping(path = "/approve/{id}", requestMethod = "PUT")
    public void activateCard(HttpExchange exchange, String id) {
        cardService.handleActivateCard(exchange, id);
    }
}
