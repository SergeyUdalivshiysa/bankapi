package model.repository;

import model.entities.Card;
import java.util.List;

public interface CardRepository {
    List<Card> getAllCards();

    void postCard(Card card);
}


