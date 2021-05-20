package model.repository.util;

import model.repository.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBaseFiller implements Repository {

    public void fill() {
        String sqlPath = "src/main/resources/init.sql";
        String content = "";
        try (Stream<String> lines = Files.lines(Paths.get(sqlPath))) {
            content = lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            executeQuery(content, ps -> {
                ps.executeUpdate();
                return null;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
