package cards;

import java.util.List;

public interface Hand {

    public List<Card> getHand();
    public void addCard(Card c);
    public Card removeCard(int c);


}
