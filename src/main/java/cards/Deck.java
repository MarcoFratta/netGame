package cards;

import java.io.IOException;
import java.util.Optional;

public interface Deck {

        //algoritmo shuffle
    void shuffle();

    Optional<Card> drawCard();

    Optional<Card> drawCard(String seme, int numero);

    int getDeckSize();

    int getLeftCardSize();
}
