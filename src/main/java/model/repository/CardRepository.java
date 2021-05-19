package model.repository;

import model.entities.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardRepository {
    List<Card> getAllCards() throws SQLException;

    void postCard(Card card) throws SQLException;
}


