package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    public final static String URL;
    public final static String DB_USER;
    public final static String DB_PASS;
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileReader("src/main/resources/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        URL = properties.getProperty("database.connection.url");
        DB_USER = properties.getProperty("database.user");
        DB_PASS = properties.getProperty("database.password");
    }
}
