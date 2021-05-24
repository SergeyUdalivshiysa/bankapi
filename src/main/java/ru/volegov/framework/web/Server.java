package ru.volegov.framework.web;

import com.sun.net.httpserver.HttpServer;
import ru.volegov.util.PropertiesManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private static HttpServer server;

    public static HttpServer getServer() {
        return server;
    }

    public static void initializeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(
                        Integer.parseInt(PropertiesManager.SERVER_PORT)),
                Integer.parseInt(PropertiesManager.SERVER_BACKLOG));
        server.setExecutor(null);
        server.start();
    }


}
