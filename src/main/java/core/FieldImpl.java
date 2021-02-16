package core;

import cards.Card;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FieldImpl implements Field {

    private final Map<Pair<Integer,Integer>, Optional<Card>> field ;
    private final int size;

    public FieldImpl(int size){
        this.size = size;
        this.field = new HashMap<>();
        this.fillField();

    }

    @Override
    public void fillField() {
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                this.field.put(new Pair<>(j,i), Optional.empty());
            }
        }
    }

    @Override
    public void addCard(Pair<Integer,Integer>pos, Card c) {
        this.field.replace(pos,Optional.of(c));
    }

    @Override
    public Optional<Card> moveCard(Pair<Integer, Integer> oldPos, Pair<Integer, Integer> newPos){
        if((newPos.getX() < 0 || newPos.getX() >= this.size) ||
                (newPos.getY() < 0 || newPos.getY() >=this.size)){
            throw new IllegalArgumentException();
        }
        Optional<Card> old = this.field.replace(oldPos,Optional.empty());
        return this.field.replace(newPos,old);

    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public boolean isFree(Pair<Integer, Integer> pos) {
        return this.field.get(pos).isEmpty();
    }

    @Override
    public Optional<Card> getCard(Pair<Integer, Integer> pos) {
        return this.field.get(pos);
    }


}
