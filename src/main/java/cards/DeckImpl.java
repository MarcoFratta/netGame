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
    private final List<Card> cardImpls;
    private final int size;

    public DeckImpl(String c, int size) throws IOException {
        this.size = size;
        this.cardImpls = new ArrayList<>();
        this.deckType = JsonCardsLoader.loadFromPath(c,this.cardImpls);
        System.out.println(this.cardImpls);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cardImpls);
    }

    @Override
    public Optional<Card> drawCard() {
        if (this.getLeftCardSize() == 0) {
            return Optional.empty();
        }
        return Optional.of(this.cardImpls.remove(this.getLeftCardSize() - 1));
    }

    @Override
    public Optional<Card> drawCard(int id) {
        return this.cardImpls.stream().filter(c -> c.getId() == id).findAny();
    }

    @Override
    public int getDeckSize() {
        return this.size;
    }

    @Override
    public int getLeftCardSize() {
        return this.cardImpls.size();
    }

    @Override
    public String getDeckType() {
        return this.deckType;
    }

}
