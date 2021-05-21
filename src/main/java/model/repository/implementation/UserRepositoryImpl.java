package model.repository.implementation;

import exception.IncorrectInputDataException;
import model.dto.CounterpartyDTO;
import model.entities.User;
import model.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final String getCounterpartiesSql = "select id, name, legal_entity from user u join counterparty c on u.id = c.counterparty_id where c.party_id = ?";
    private final String insertUserSql = "insert into user (name, legal_entity) values (?, ?)";
    private final String addCounterpartySql = "insert into counterparty (party_id, counterparty_id) values (?, ?)";

    @Override
    public List<User> findCounterpartiesById(String id) throws SQLException {
        List<User> users = new ArrayList<>();
        return executeQuery(getCounterpartiesSql, statement -> {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        });
    }

    @Override
    public void addCounterparty(CounterpartyDTO counterpartyDTO) throws SQLException {
        executeQuery(addCounterpartySql, statement -> {
            statement.setInt(1, counterpartyDTO.getPartyId());
            statement.setInt(2, counterpartyDTO.getCounterpartyId());
            int result = statement.executeUpdate();
            if (result < 1) throw new IncorrectInputDataException("Incorrect input data");
            return null;
        });
    }

    @Override
    public void addUser(User user) throws SQLException {
        executeQuery(insertUserSql, statement -> {
            statement.setString(1, user.getName());
            statement.setBoolean(2, user.isLegalEntity());
            statement.executeUpdate();
            return null;
        });
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBoolean(3));
    }
}
