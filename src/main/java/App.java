import controller.CardController;
import framework.ApplicationContext;
import model.repository.DataBaseFiller;
import web.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;


public class App {
    public static void main(String[] args) throws IOException {
        Server.initializeServer();
        DataBaseFiller.fill();
        try {
            ApplicationContext.initializeContext();
        } catch (URISyntaxException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            System.out.println("Ошибка инициализации контекста");
            e.printStackTrace();
        }

    }


}
