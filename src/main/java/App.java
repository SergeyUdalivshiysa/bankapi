import framework.ApplicationContext;
import framework.implementation.ApplicationContextImpl;
import model.repository.DataBaseFiller;
import web.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;


public class App {
    public static void main(String[] args) {
        try {
            Server.initializeServer();
        } catch (IOException e) {
            System.out.println("Ошибка инициализакии сервера");
            e.printStackTrace();
        }
        DataBaseFiller.fill();
        try {
            ApplicationContext applicationContext = new ApplicationContextImpl();
            applicationContext.initializeContext();
        } catch (Exception e) {
            System.out.println("Ошибка инициализации контекста");
            e.printStackTrace();
        }
    }
}
