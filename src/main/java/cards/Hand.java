package cards;

import java.util.List;
import java.util.Optional;

public interface Hand {

    List<Optional<Card>> getHand();
    void addCard(Card c,int pos);
    Optional<Card> removeCard(int c);
    Optional<Card>removeCardFromId(int id);
    void addCard(Card card);
}
