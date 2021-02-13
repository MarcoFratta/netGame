package cards;

import java.util.Optional;

public interface Deck {

        //algoritmo shuffle
    void shuffle();

    Optional<Card> drawCard();

    Optional<Card> drawCard(int id);

    int getDeckSize();

    int getLeftCardSize();

    String getDeckType();
}
