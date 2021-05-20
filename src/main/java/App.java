import framework.ApplicationContext;
import framework.implementation.ApplicationContextImpl;
import framework.web.Server;
import model.repository.util.DataBaseFiller;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Server.initializeServer();
            System.out.println("Сервер запущен.");
        } catch (IOException e) {
            System.out.println("Ошибка инициализакии сервера");
            e.printStackTrace();
        }
        DataBaseFiller dataBaseFiller = new DataBaseFiller();
        dataBaseFiller.fill();
        try {
            ApplicationContext applicationContext = new ApplicationContextImpl();
            applicationContext.initializeContext();
            System.out.println("Контекст инициализирован.");
        } catch (Exception e) {
            System.out.println("Ошибка инициализации контекста");
            e.printStackTrace();
        }
    }
}
