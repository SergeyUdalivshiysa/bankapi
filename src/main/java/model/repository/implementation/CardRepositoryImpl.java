package model.repository.implementation;

import model.entities.Card;
import model.repository.CardRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {

    private final String getSql = "select * from card";
    private final String getUnapprovedCardsSql = "select * from card where active = false";
    private final String insertCardSql = "insert into card (number, account_id, active) values (?, (select id from account where id = ?), ?) ";
    private final String activateCardSql = "update card set active = true where id = ?";

    @Override
    public List<Card> getAllCards() throws SQLException {
        return getCards(getSql);
    }

    @Override
    public void postCard(Card card) throws SQLException {
        executeQuery(insertCardSql, statement -> {
            statement.setString(1, card.getNumber());
            statement.setInt(2, card.getAccount_id());
            statement.setBoolean(3, card.isActive());
            statement.executeUpdate();
            return null;
        });
    }

    @Override
    public List<Card> getUnapprovedCards() throws SQLException {
        return getCards(getUnapprovedCardsSql);
    }

    @Override
    public void activateCard(String id) throws SQLException {
        executeQuery(activateCardSql, statement -> {
            statement.setString(1, id);
            int result = statement.executeUpdate();
            if (result < 1) throw new SQLException("Wrong input data");
            return null;
        });
    }

    private Card buildCard(ResultSet resultSet) throws SQLException {
        return new Card(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getBoolean(4));
    }

    private List<Card> getCards(String sql) throws SQLException {
        return executeQuery(sql, statement -> {
            List<Card> cards = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cards.add(buildCard(resultSet));
            }
            return cards;
        });
    }
}
