package web;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private static HttpServer server;

    public static HttpServer getServer() {
        return server;
    }

    public static void initializeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(null);
        server.start();
    }


}
