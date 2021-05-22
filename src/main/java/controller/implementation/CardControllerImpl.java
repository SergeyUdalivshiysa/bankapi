package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.CardController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.CardService;
import model.service.implementation.CardServiceImpl;

@Controller(path = "/cards")
public class CardControllerImpl extends AbstractController implements CardController {

    private final CardService cardService;

    public CardControllerImpl() {
        cardService = new CardServiceImpl();
    }

    @Override
    @RequestMapping(path = "/{id}", requestMethod = "GET")
    public void findCardsByAccountId(HttpExchange exchange, String id) {
        cardService.handleFindCardsByAccountId(exchange, id);
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
