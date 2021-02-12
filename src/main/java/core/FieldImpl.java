package core;

import cards.Card;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FieldImpl implements Field {

    private Map<Pair<Integer,Integer>, Optional<Card>> field ;
    private final int size;

    public FieldImpl(int size){
        this.size = size;
        field = new HashMap<>();
        fillField();

    }

    @Override
    public void fillField() {
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < size ; j++){
                field.put(new Pair<>(j,i), Optional.empty());
            }
        }
    }

    @Override
    public void addCard(Pair<Integer,Integer>pos, Card c) {
        this.field.replace(pos,Optional.of(c));
    }

    @Override
    public Optional<Card> moveCard(Card newCardImpl, Pair<Integer, Integer> newPosition){
        if((newPosition.getX() < 0 || newPosition.getX() >= this.size) ||
                (newPosition.getY() < 0 || newPosition.getY() >=this.size)){
            throw new IllegalArgumentException();
        }
        Optional<Card> tmp = field.get(newPosition);
        field.replace(newPosition,Optional.of(newCardImpl));
        return tmp;
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
