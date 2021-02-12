package cards;

import java.util.Optional;

public interface Deck {

        //algoritmo shuffle
    void shuffle();

    Optional<Card> drawCard();

    Optional<Card> drawCard(String seed, int number);

    int getDeckSize();

    int getLeftCardSize();

    String getDeckType();
}
