package ru.volegov.controller.client.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.AbstractController;
import ru.volegov.controller.client.CardController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.CardService;
import ru.volegov.model.service.implementation.CardServiceImpl;

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
}
