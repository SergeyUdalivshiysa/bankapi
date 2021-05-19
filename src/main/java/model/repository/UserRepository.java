package model.repository;

import model.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    List<User> findAllCounterparties(String id) throws SQLException;

    void postUserAsCounterparty(User user) throws SQLException;

}
