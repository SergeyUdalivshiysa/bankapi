package ru.volegov.model.repository.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.volegov.model.dto.CounterpartyDTO;
import ru.volegov.model.entities.User;
import ru.volegov.model.repository.UserRepository;
import ru.volegov.model.repository.util.DataBaseFiller;
import ru.volegov.util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class UserRepositoryImplTest {

    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getCounterpartySql = "select id, name, legal_entity from user u join counterparty c on u.id = c.counterparty_id where party_id = ?";
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final String getLastCreatedUserSql = "select id, name, legal_entity from user order by id desc limit 1";

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void findCounterpartiesById() {
        try {
            List<User> expectedUsers = new ArrayList<>();
            User user = new User(2, "Vlad", false);
            expectedUsers.add(user);
            List<User> actualUsers = userRepository.findCounterpartiesById(1);
            Assertions.assertEquals(expectedUsers, actualUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addCounterparty() {
        try {
            int partyId = 3;
            int counterpartyId = 2;
            CounterpartyDTO counterpartyDTO = new CounterpartyDTO(partyId, counterpartyId);
            userRepository.addCounterparty(counterpartyDTO);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getCounterpartySql);
            preparedStatement.setInt(1, partyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            User actualUser = null;
            if (resultSet.next()) actualUser = buildUser(resultSet);
            User expectedUser = new User(2, "Vlad", false);
            Assertions.assertEquals(expectedUser, actualUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBoolean(3));
    }

    @Test
    void addUser() {
        try {
            User user = new User(4, "Slava", true);
            userRepository.addUser(user);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedUserSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            User actualUser = null;
            if (resultSet.next()) actualUser = buildUser(resultSet);
            preparedStatement.close();
            connection.close();
            User expectedUser = new User(4, "Slava", true);
            Assertions.assertEquals(expectedUser, actualUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}