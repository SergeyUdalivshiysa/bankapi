package model.repository.implementation;

import exception.NotFoundException;
import model.dto.CardDTO;
import model.entities.Card;
import model.repository.CardRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {

    private final String getCardsByAccountId = "select c.id, c.number, account_id, active from card c join account a on c.account_id = a.id where a.id = ?";
    private final String getUnapprovedCardsSql = "select id, number, account_id, active from card where active = false";
    private final String insertCardSql = "insert into card (account_id, active) values ((select id from account where id = ?), ?) ";
    private final String activateCardSql = "update card set active = true where id = ? and active = false";

    @Override
    public List<Card> getCardsByAccountId(int id) throws SQLException {
        return executeQuery(getCardsByAccountId, statement -> {
            statement.setInt(1, id);
            List<Card> cards = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cards.add(buildCard(resultSet));
            }
            if (cards.isEmpty()) throw new NotFoundException("No cards were founded");
            return cards;
        });
    }

    @Override
    public void addCard(CardDTO dto) throws SQLException {
        executeQuery(insertCardSql, statement -> {
            statement.setInt(1, dto.getAccountId());
            statement.setBoolean(2, false);
            statement.executeUpdate();
            return null;
        });
    }

    @Override
    public List<Card> getUnapprovedCards() throws SQLException {
        return getCards(getUnapprovedCardsSql);
    }

    @Override
    public void activateCard(int id) throws SQLException {
        executeQuery(activateCardSql, statement -> {
            statement.setInt(1, id);
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
