package core;

import cards.*;
import rules.GameRules;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultLogic implements Logic {

    private Field field;
    private Map<Player, Hand> players;
    private Deck deck;
    private GameRules gameRules;
    private Player turnPlayer;

    public DefaultLogic(GameRules gameRules){
        this.gameRules = gameRules;
    }

    @Override
    public void startGame(List<Player> list) {
        if(list.size() < gameRules.getMinPLayers() || list.size() > gameRules.getMaxPLayers()){
            throw new IllegalArgumentException();
        }

        this.players = list.stream().
                collect(Collectors.toMap(a->a , a-> new HandImpl(gameRules.getHandSize())));


       this.deck = new DeckBuilder()
               .path(gameRules.getDeckPath())
               .size(gameRules.getDeckSize())
               .shuffled()
               .build();

       this.field = new FieldFactoryImpl().emptyField(gameRules.getFieldSize());
       fillHands();
       System.out.println(this.players.values().toString());
    }

    private void fillHands() {
        this.players.forEach((key, value) -> Stream.iterate(0, a -> a + 1)
                .limit(gameRules.getHandSize())
                .forEach(a -> draw(key)));
    }

    private void draw(Player p){
        if(deck.getLeftCardSize() > 0)
        {
            Optional<Card> c = deck.drawCard();
            c.ifPresent(card -> players.get(p).addCard(card));
        }
    }

    public List<Hand> getHands(){
        return List.copyOf(this.players.values());
    }
    public Field getField(){
        return this.field;
    }
}
