package ru.volegov.model.repository;

import ru.volegov.model.dto.CounterpartyDTO;
import ru.volegov.model.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends Repository {
    /**
     * Handles finding all the counterparties of a certain ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    List<User> findCounterpartiesById(int id) throws SQLException;

    /**
     * Handles adding a user
     *
     * @param user
     * @throws SQLException
     */
    void addUser(User user) throws SQLException;

    /**
     * Handles adding a counterparty
     *
     * @param counterpartyDTO
     * @throws SQLException
     */
    void addCounterparty(CounterpartyDTO counterpartyDTO) throws SQLException;

}
