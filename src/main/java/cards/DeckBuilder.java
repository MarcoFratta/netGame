package cards;

import java.io.IOException;
import java.util.Optional;

public class DeckBuilder {

    private Optional<String> deckPath;
    private Optional<Integer> size;
    private boolean shuffle;


    public DeckBuilder(){
        this.deckPath = Optional.empty();
        this.size = Optional.empty();
        this.shuffle = false;
    }

    public DeckBuilder path(String c){
        if(this.deckPath.isEmpty()){
            this.deckPath = Optional.of(c);
        } else {
            throw new IllegalStateException();
        }
        return this;
    }
     public DeckBuilder size(int size){
        if(this.size.isEmpty()) {
                this.size = Optional.of(size);
        } else {
            throw new IllegalStateException();
        }
        return this;
     }
    public DeckBuilder shuffled(){
        this.shuffle = true;
        return this;
    }

     public Deck build(){
        if(this.size.isEmpty() || this.deckPath.isEmpty()){
            throw new IllegalStateException();
        }
         Deck deck = null;
         try {
             deck = new DeckImpl(this.deckPath.get(), this.size.get());
             if(this.shuffle){
                 deck.shuffle();
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return deck;
    }
}


