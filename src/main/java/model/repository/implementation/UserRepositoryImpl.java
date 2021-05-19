package model.repository.implementation;

import model.entities.User;
import model.repository.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final String URL = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
    private final String GET_COUNTERPARTIES_SQL = "select * from user where counterparty = true and partner_id  = ?";
    private final String INSERT_USER_SQL = "insert into user (name, counterparty, legal_entity, partner_id) values (?, ?, ?, ?)";

    @Override
    public List<User> findAllCounterparties(String id) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(GET_COUNTERPARTIES_SQL);
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
        }
        return users;
    }

    public void postUserAsCounterparty(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
            ps.setString(1, user.getName());
            ps.setBoolean(2, user.isCounterparty());
            ps.setBoolean(3, user.isLegalEntity());
            ps.setInt(4, user.getPartnerId());
            ps.executeUpdate();
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBoolean(3),
                resultSet.getBoolean(4),
                resultSet.getInt(5));
    }

}
