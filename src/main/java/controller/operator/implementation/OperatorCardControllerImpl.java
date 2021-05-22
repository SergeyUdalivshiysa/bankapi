package controller.operator.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.operator.AbstractControllerWithAuthorization;
import controller.operator.OperatorCardController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.CardService;
import model.service.implementation.CardServiceImpl;

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
