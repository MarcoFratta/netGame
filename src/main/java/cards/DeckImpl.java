/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;
import java.io.IOException;
import java.util.*;


public class DeckImpl implements Deck {
    public final static int DEFUALT_CARDS_NUMBER = 40;
    private final String deckType;
    private final List<Card> cards;
    private final int size;
    private final int loadedDeckSize;

    public DeckImpl(String c, int size) throws IOException {
        this.size = size;
        this.cards = new ArrayList<>();
        this.deckType = JsonCardsLoader.loadFromPath(c, this.cards);
        this.loadedDeckSize = this.cards.size();
        System.out.println(this.cards);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    @Override
    public Optional<Card> drawCard() {
        if (this.getLeftCardSize() == 0) {
            return Optional.empty();
        }
        return Optional.of(this.cards.remove(this.getLeftCardSize() - 1));
    }

    @Override
    public Optional<Card> drawCard(int id) {
        return this.cards.stream().filter(c -> c.getId() == id).findAny();
    }

    @Override
    public int getDeckSize() {
        return this.size;
    }

    @Override
    public int getLeftCardSize() {
        return this.size - (this.loadedDeckSize - this.cards.size());
    }

    @Override
    public String getDeckType() {
        return this.deckType;
    }

}
