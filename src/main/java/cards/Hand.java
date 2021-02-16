package cards;

import java.util.List;
import java.util.Optional;

public interface Hand {

    List<Optional<Card>> getHand();
    Optional<Card> removeCard(int c);
    Optional<Card> removeCardFromId(int id);

    void setCard(Card c);
}
