package cards;

import java.util.*;
import java.util.stream.Collectors;

public class HandImpl implements Hand{


    private final List<Optional<Card>> hand;
    private final int size;

    public HandImpl(List<Card> hand, int size) {
        this.hand = hand.stream().map(Optional::of).collect(Collectors.toList());
        this.size = size;
    }

    public HandImpl(int size){
        this(Collections.emptyList(),size);
    }

    @Override
    public List<Optional<Card>> getHand() {
        return this.hand;
    }

    @Override
    public void setCard(Card c) {
        if (this.hand.size() != this.size) {
            this.addCard(c);
            return;
        }
        int pos = 0;
        while (pos < this.size && this.hand.get(pos).isPresent()) {
            pos++;
        }
        if (pos < this.size) {
            this.hand.set(pos, Optional.of(c));
        }

    }

    @Override
    public Optional<Card> removeCard(int c) {
        Optional<Card> card = this.hand.get(c);
        this.hand.set(c, Optional.empty());
        return card;
    }

    @Override
    public Optional<Card> removeCardFromId(int id) {
        Optional<Card> card = this.hand.stream().filter(Optional::isPresent)
                .map(Optional::get).filter(c->c.getId() == id).findFirst();
        return card.isPresent() ? this.removeCard(this.hand.indexOf(card)) : Optional.empty();
    }

    private void addCard(Card card) {
        this.hand.add(Optional.of(card));
    }

    public void sort(){
        this.hand.sort((a,b)-> a.map(value ->
                (b.map(card -> value.getNumber() - card.getNumber()).orElse(1))).orElse(-1));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Hand(3)>"+ this.hand.toString();
    }
}
