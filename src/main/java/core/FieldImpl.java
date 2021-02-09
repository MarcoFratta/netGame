package core;

import cards.Card;

import java.util.Map;
import java.util.Optional;

public class FieldImpl implements Field{

    private Map<Pair<Integer,Integer>, Optional<Card>> field ;
    private final int size;

    public FieldImpl(int size){
        this.size = size;

    }
    private void fillField(){
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < size ; j++){
                field.put(new Pair<>(j,i),Optional.empty());
            }
        }
    }

    public Optional<Card> moveCard(Card newCard , Pair<Integer,Integer> newPosition){
        if((newPosition.getX() < 0 || newPosition.getX() >= this.size) ||
                (newPosition.getY() < 0 || newPosition.getY() >=this.size)){
            throw new IllegalArgumentException();
        }
        Optional<Card> tmp = field.get(newPosition);
        field.replace(newPosition,Optional.of(newCard));
        return tmp;
    }

}
