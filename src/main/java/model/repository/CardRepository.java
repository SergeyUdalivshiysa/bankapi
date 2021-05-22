package model.repository;

import model.dto.CardDTO;
import model.entities.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardRepository extends Repository {

    List<Card> getCardsByAccountId(int id) throws SQLException;

    void addCard(CardDTO dto) throws SQLException;

    List<Card> getUnapprovedCards() throws SQLException;

    void activateCard(int id) throws SQLException;
}


