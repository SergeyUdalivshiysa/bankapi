package model.repository.util;

import model.repository.Repository;
import org.apache.maven.model.Model;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class DataBaseFiller implements Repository {
    public void fill() {
        InputStream in = Model.class.getClassLoader().getResourceAsStream("init.sql");
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            executeQuery(textBuilder.toString(), ps -> {
                ps.executeUpdate();
                return null;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
