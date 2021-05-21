package model.repository;

import util.PropertiesManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Repository {
    default <R> R executeQuery(String query, Executor<R> executor) throws SQLException {
        try (Connection connection = DriverManager.getConnection(PropertiesManager.URL);
             PreparedStatement ps = connection.prepareStatement(query)) {
            return executor.apply(ps);
        }
    }

    @FunctionalInterface
    interface Executor<R> {
        R apply(PreparedStatement statement) throws SQLException;
    }

}
