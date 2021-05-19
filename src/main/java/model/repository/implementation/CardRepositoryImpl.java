package model.repository.implementation;

import model.entities.Card;
import model.repository.CardRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {

    private final String URL = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
    private final String GET_SQL = "select * from card";
    private final String INSERT_CARD_SQL = "insert into card (number, account_id, active) values (?, (select id from account where id = ?), ?) ";

    public List<Card> getAllCards() throws SQLException {
        List<Card> cards = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(GET_SQL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                cards.add(buildCard(resultSet));
            }
        }
        return cards;
    }

    public void postCard(Card card) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(INSERT_CARD_SQL);
            ps.setString(1, card.getNumber());
            ps.setInt(2, card.getAccount_id());
            ps.setBoolean(3, card.isActive());
            ps.executeUpdate();
        }
    }

    private Card buildCard(ResultSet resultSet) throws SQLException {
        return new Card(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getBoolean(4));
    }
}
