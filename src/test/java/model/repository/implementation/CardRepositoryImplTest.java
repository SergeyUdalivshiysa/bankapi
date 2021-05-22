package model.repository.implementation;

import model.dto.CardDTO;
import model.entities.Card;
import model.repository.CardRepository;
import model.repository.util.DataBaseFiller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardRepositoryImplTest {


    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedCard = "select id, number, account_id, active from card order by id desc limit 1";
    private final String getIsApproved = "select active from card where id = ?";
    private final CardRepository cardRepository = new CardRepositoryImpl();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void getCardsByAccountId() {
        try {
            int accountId = 1;
            List<Card> actualCards = cardRepository.getCardsByAccountId(accountId);
            List<Card> expectedCards = new ArrayList<>();
            Card card1 = new Card(1, "1000000000000000", 1, true);
            Card card2 = new Card(2, "1000000000000001", 1, false);
            expectedCards.add(card1);
            expectedCards.add(card2);
            assertEquals(expectedCards, actualCards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addCard() {
        try {
            CardDTO cardDTO = new CardDTO(2);
            cardRepository.addCard(cardDTO);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedCard);
            ResultSet resultSet = preparedStatement.executeQuery();
            Card actualCard = null;
            if (resultSet.next()) actualCard = buildCard(resultSet);
            preparedStatement.close();
            connection.close();
            Card expectedCard = new Card(3, "1000000000000002", 2, false);
            assertEquals(expectedCard, actualCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Card buildCard(ResultSet resultSet) throws SQLException {
        return new Card(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getBoolean(4));
    }

    @Test
    void getUnapprovedCards() {
        try {
            Card card1 = new Card(2, "1000000000000001", 1, false);
            List<Card> expectedCards = new ArrayList<>();
            expectedCards.add(card1);
            List<Card> actualCards = cardRepository.getUnapprovedCards();
            assertEquals(expectedCards, actualCards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void activateCard() {
        try {
            int id = 2;
            cardRepository.activateCard(id);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getIsApproved);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean actual = false;
            if (resultSet.next()) actual = resultSet.getBoolean(1);
            preparedStatement.close();
            connection.close();
            assertTrue(actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}