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
    private final String  deckPath;
    private List<Card> cards;
    private final int size;

    public DeckImpl(String c, int size) throws IOException {
        this.size = size;
        this.deckPath = c;
        this.cards = new ArrayList<>();
        JsonCardsLoader.loadFromPath(deckPath,this.cards);
        //generate();
        System.out.println(cards);
    }
    //algoritmo shuffle
    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Optional<Card> drawCard() {
        if (getLeftCardSize() == 0) {
            return Optional.empty();
        }
        return Optional.of(cards.remove(getLeftCardSize() - 1));
    }

    @Override
    public Optional<Card> drawCard(String seed, int number) {
        return cards.stream().filter(c -> c.getNumber() ==number && c.getSeed().equals(seed)).findAny();
    }

    @Override
    public int getDeckSize() {
        return size;
    }

    @Override
    public int getLeftCardSize() {
        return this.cards.size();
    }

}
