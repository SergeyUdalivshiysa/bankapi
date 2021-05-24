package ru.volegov.controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import ru.volegov.controller.operator.AbstractControllerWithAuthorization;
import ru.volegov.controller.operator.OperatorCardController;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.framework.annotations.RequestMapping;
import ru.volegov.model.service.CardService;
import ru.volegov.model.service.implementation.CardServiceImpl;

@Controller(path = "/operator/cards")
public class OperatorCardControllerImpl extends AbstractControllerWithAuthorization implements OperatorCardController {

    private final CardService cardService;

    public OperatorCardControllerImpl() {
        cardService = new CardServiceImpl();
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
