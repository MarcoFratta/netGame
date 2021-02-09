package cards;

import cards.Card;

import java.util.List;

public interface Hand {

    public List<Card> getHand();
    public void addCard(Card c);
    public void removeCard(Card c);


}
