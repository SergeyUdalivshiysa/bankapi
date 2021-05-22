package model.repository;

import model.dto.CardDTO;
import model.entities.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardRepository extends Repository {

    /**
     * Hanles returning all of the cards of a certain account
     *
     * @param id
     * @return
     * @throws SQLException
     */
    List<Card> getCardsByAccountId(int id) throws SQLException;

    /**
     * Handles addling a card
     *
     * @param dto
     * @throws SQLException
     */
    void addCard(CardDTO dto) throws SQLException;

    /**
     * Handles returning all the unactivated cards
     *
     * @return
     * @throws SQLException
     */
    List<Card> getUnapprovedCards() throws SQLException;

    /**
     * Handles card activation
     *
     * @param id
     * @throws SQLException
     */
    void activateCard(int id) throws SQLException;
}


