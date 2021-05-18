package model.repository.implementation;

import model.entities.Card;
import model.repository.CardRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {

    private final String URL = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
    private final String GET_SQL = "select * from card";
    private final String POST_SQL = "insert into card (number, account_id, active) values (?, ?, ?) ";

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(GET_SQL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getBoolean(4));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public void postCard(Card card) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(POST_SQL);
            ps.setString(1, card.getNumber());
            ps.setInt(2, card.getAccount_id());
            ps.setBoolean(3, card.isActive());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
