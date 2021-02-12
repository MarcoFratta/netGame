package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HandImpl implements Hand{


    private final List<Card> hand;
    private final int size;

    public HandImpl(List<Card> hand, int size) {
        this.hand = new ArrayList<>(hand);
        this.size = size;
    }

    public HandImpl(int size){
        this(Collections.emptyList(),size);
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
    public void addCard(Card c) {
        if(this.hand.size() == this.size){
            throw new IllegalStateException();
        }
        this.hand.add(c);
    }

    @Override
    public Card removeCard(int c) {
        return this.hand.remove(c);
    }

    public void sort(){
        this.hand.sort(Comparator.comparingInt(Card::getNumber));
    }

    @Override
    public String toString() {
        return "Hand(3)>"+ hand.toString();
    }
}
