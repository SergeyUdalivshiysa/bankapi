package ru.volegov.controller.operator;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ru.volegov.controller.AbstractController;
import ru.volegov.framework.web.Server;

public abstract class AbstractControllerWithAuthorization extends AbstractController {
    @Override
    public void initialize() {
        Authenticator authenticator = new BasicAuthenticator("operator") {
            @Override
            public boolean checkCredentials(String username, String password) {
                return username.equals("admin") && password.equals("admin");
            }
        };
        HttpServer server = Server.getServer();
        HttpContext context = server.createContext(basePath, this::dispatch);
        context.setAuthenticator(authenticator);
    }
}
