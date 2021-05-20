package model.repository;

import model.entities.User;
import model.repository.dto.CounterpartyDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends Repository {

    List<User> findCounterpartiesById(String id) throws SQLException;

    void addUser(User user) throws SQLException;

    void addCounterparty(CounterpartyDTO counterpartyDTO) throws SQLException;

}
