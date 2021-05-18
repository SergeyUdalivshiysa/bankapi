package model.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBaseFiller {
    public static void fill() {
        String path = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
        String sqlPath = "src/main/resources/init.sql";
        String content = "";
        try (Stream<String> lines = Files.lines(Paths.get(sqlPath))) {
            content = lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(path)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(content);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
