package core;

import cards.Card;

import java.util.Optional;

public interface Field {
     void fillField();
     void addCard(Pair<Integer,Integer>pos, Card c);

    Optional<Card> moveCard(Card newCardImpl, Pair<Integer, Integer> newPosition);

    Integer getSize();

    boolean isFree(Pair<Integer, Integer> pos);

    Optional<Card> getCard(Pair<Integer, Integer> pos);
}
