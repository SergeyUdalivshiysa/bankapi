package ru.volegov.util;

import org.apache.maven.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    public final static String URL;
    public final static String DB_USER;
    public final static String DB_PASS;
    public final static String CONTROLLER_PATH;
    public final static String SERVER_PORT;
    public final static String SERVER_BACKLOG;
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream in = Model.class.getClassLoader().getResourceAsStream("app.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SERVER_PORT = properties.getProperty("server.port");
        SERVER_BACKLOG = properties.getProperty("server.backlog");
        CONTROLLER_PATH = properties.getProperty("controller.package.path");
        URL = properties.getProperty("database.connection.url");
        DB_USER = properties.getProperty("database.user");
        DB_PASS = properties.getProperty("database.password");
    }
}
