package model.repository;

import model.dto.CounterpartyDTO;
import model.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends Repository {

    List<User> findCounterpartiesById(int id) throws SQLException;

    void addUser(User user) throws SQLException;

    void addCounterparty(CounterpartyDTO counterpartyDTO) throws SQLException;

}
