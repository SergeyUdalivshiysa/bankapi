package controller.implementation;

import com.sun.net.httpserver.HttpExchange;
import controller.CardController;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import model.service.CardService;
import model.service.implementation.CardServiceImpl;

@Controller(path = "/cards")
public class CardControllerImplImpl extends AbstractControllerImpl implements CardController {

    private final CardService CARD_SERVICE = new CardServiceImpl();

    @RequestMapping(path = "/", requestMethod = "GET")
    public void findAll(HttpExchange exchange) {
       CARD_SERVICE.handleFindAll(exchange);
    }

    //Test
    @RequestMapping(path = "/test/{id}", requestMethod = "GET")
    public void getCardById(HttpExchange exchange, String id) {
        System.out.println(id);
    }

    @RequestMapping(path = "/", requestMethod = "POST")
    public void createCard(HttpExchange exchange) {
        CARD_SERVICE.handleCreateCard(exchange);
    }
}
