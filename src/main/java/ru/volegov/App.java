package ru.volegov;

import ru.volegov.framework.ApplicationContext;
import ru.volegov.framework.implementation.ApplicationContextImpl;
import ru.volegov.framework.web.Server;
import ru.volegov.model.repository.util.DataBaseFiller;
import ru.volegov.util.InfoPrinter;

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
            InfoPrinter.printInfo();
        } catch (Exception e) {
            System.out.println("Ошибка инициализации контекста");
            e.printStackTrace();
        }
    }
}
