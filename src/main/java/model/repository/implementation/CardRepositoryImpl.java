package model.repository.implementation;

import model.entities.Card;
import model.repository.CardRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {
    public List<Card> getAllCards(){
        List<Card> cards = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd");) {
            PreparedStatement ps = connection.prepareStatement("select * from card");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBoolean(4));
                cards.add(card);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cards;
    }

    public void postCard(Card card){
        String sql = "insert into card (number, account_id, active) values (?, ?, ?) ";
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd")) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, card.getNumber());
            ps.setInt(2, card.getAccount_id());
            ps.setBoolean(3, card.isActive());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}