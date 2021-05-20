package model.repository;

import model.entities.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardRepository extends Repository {

    List<Card> getAllCards() throws SQLException;

    void postCard(Card card) throws SQLException;

    List<Card> getUnapprovedCards() throws SQLException;

    void activateCard(String id) throws SQLException;
}


