package core;

import cards.*;
import net.Comunicator;
import packets.*;
import views.GameController;

import java.util.*;
import java.util.stream.Collectors;

public class LocalLogic implements Logic {

    public static final int NO_HAND_SELECTED = -1;


    public static final String CARDS_PACKAGE = "/cards_file/";
    public static final String FILE_TYPE = ".json";
    private final List<? extends Player> players;
    private final Hand hand;
    private final Comunicator server;
    private final Deck deck;
    private final Field field;
    private final List<Card> removedCards;
    private GameController controller;
    private boolean isMyTurn = false;
    private Player player;
    private Pair<Integer,Integer> selectedFieldCard;
    private int selectedHandCard = NO_HAND_SELECTED;

    public LocalLogic(GameInfoPacket packet,Comunicator server) {
        this.players = packet.getPlayers();
        this.server = server;
        this.removedCards = new ArrayList<>();
        try {
            this.player = this.players.stream().filter(p -> p.getId() == packet.getDestPlayerId())
                    .findAny().get();
        }catch (NoSuchElementException e){
            server.send(new ErrorPacket("id error"));
        }
        try{
            String path = this.getClass().getResource(CARDS_PACKAGE +packet.getDeckType()+ FILE_TYPE).getFile();
            this.deck = new DeckImpl(path, packet.getDeckSize());
        }catch (Exception e){
            server.send(new ErrorPacket("deck error"));
            throw new IllegalArgumentException();
        }

        this.hand = new HandImpl(packet.getHandSize());
        this.field = new FieldImpl(packet.getFieldSize());
    }

    @Override
    public void startGame() {
        System.out.println("Sending ready message "+ this.player);
        this.server.send(new ReadyForHandPacket(this.player.getId()));
        this.waitForHand();
        this.showHand();
        this.waitForTurn();
    }
    private void showHand() {
        for(int i = 0; i < this.hand.getHand().size(); i++){
            this.controller.addHandCard(this.hand.getHand().get(i),i);
        }

    }
    private void waitForHand() {
        System.out.println("Waiting for hand "+ this.player.toString());
        Object p = this.server.receive();
        if(p instanceof HandPacket){
            List<Card> cards = ((HandPacket) p).getCards();
            int index = 0;
            for (Card c : cards) {
                try {
                    Card card = this.deck.drawCard(c.getId()).get();
                    this.hand.addCard(card,index++);
                } catch (NoSuchElementException e) {
                    System.out.println("starting hand error");
                }
            }
        }
    }
    public void handTick(int pos) {
        if(this.selectedHandCard!=pos) {
            this.selectedHandCard = pos;
            this.unSelectField();
            this.controller.selectHand(pos);
            this.controller.clearFieldSelections();
            this.hand.getHand().get(this.selectedHandCard).ifPresent(c -> this.controller.selectCells(this.getValidCells(State.HAND)));
        } else {
            this.unselectedHand();
            this.controller.clearHandSelections();
        }
    }
    public void unselectedHand(){
        this.selectedHandCard = NO_HAND_SELECTED;
    }
    public void unSelectField(){
        this.selectedFieldCard = null;
    }
    public void fieldTick(Pair<Integer,Integer> pos){
        //System.out.println("BEOFRE FIELD CLICK -> "+this.selectedHandCard+" ->"+this.selectedFieldCard);
        if(this.selectedHandCard!= NO_HAND_SELECTED){ // want to move from hand to field
            if(this.isValid(pos,State.HAND)){
                Card c = this.hand.removeCard(this.selectedHandCard).get();
                this.server.send(new MoveFromHandPacket(c, pos));
                this.moveFromHandToField(c,pos);
                this.adjustView();
            }
        }else if(this.selectedFieldCard!=null){ // want to move from field to field
            if(this.isValid(pos,State.FIELD)){
               // System.out.println("Moving from field to field >"+ this.player);
                this.server.send(new MoveFromFieldPacket(new Pair<>(this.selectedFieldCard), pos));
                this.moveFromFieldToField(pos);
                this.adjustView();
            }
        } else if(!this.field.isFree(pos)){
            this.unselectedHand(); // start field card selected
                this.controller.clearHandSelections();
                this.selectedFieldCard = pos;
                this.controller.selectCells(this.getValidCells(State.FIELD));
        }
        //System.out.println("AFTER FIELD CLICK -> "+this.selectedHandCard+" ->"+this.selectedFieldCard);
    }
    private void moveFromFieldToField(Pair<Integer, Integer> pos) {
        Optional<Card> f = this.field.moveCard(this.selectedFieldCard, pos);
        f.ifPresent(card -> this.removedCards.add(f.get()));
        this.moveCardOnField(pos);
    }
    private void moveFromHandToField(Card c,Pair<Integer, Integer> pos) {
        this.field.addCard(pos,c);
        this.addCardFromHandToView(pos);
    }
    private void adjustView() {
        this.unSelectField();
        this.unselectedHand();
        this.isMyTurn = false;
        this.controller.clearFieldSelections();
        this.controller.setCanPlay(false);
        this.waitForTurn();

    }
    private void addCardFromHandToView(Pair<Integer, Integer> pos) {
        this.controller.addCardToField(this.field.getCard(pos).get(), pos);
        if(this.selectedHandCard!=NO_HAND_SELECTED) {
            this.controller.removeCardFromHand(this.selectedHandCard);
        }
    }
    private void moveCardOnField(Pair<Integer, Integer> pos) {
        this.controller.removeCardFromField(pos);
        this.controller.removeCardFromField(this.selectedFieldCard);
        this.controller.addCardToField(this.field.getCard(pos).get(),pos); // cant be null
    }
    private List<Pair<Integer, Integer>> getValidCells(State state) {
        //field.moveCard()
        try {
            if (state.equals(State.HAND)) {
                return this.hand.
                        getHand().get(this.selectedHandCard).
                        get().getMovementManager().getDestinations(state, new Pair<>(-1, -1), this.field.getSize())
                        .stream().filter(this.field::isFree).collect(Collectors.toList());
            } else if(state.equals(State.FIELD)){
                List<Pair<Integer,Integer>> selectedCells = new ArrayList<>();
                selectedCells.add(this.selectedFieldCard);
                return selectedCells;
            }
        }catch (Exception e){
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
    private void waitForTurn() {
        new Thread(()->{
        while(!this.isMyTurn) {
            System.out.println("Waiting turn... "+this.player);
            Object o = this.server.receive();
            System.out.println("Packet received- "+this.player+" p->"+o);
            if (o instanceof TurnPacket) {
                this.isMyTurn = true;
                this.controller.setCanPlay(true);
            } else if (o instanceof AddedFromHandPacket) {
                AddedFromHandPacket p = (AddedFromHandPacket) o;
                this.selectedHandCard = NO_HAND_SELECTED;
                this.moveFromHandToField(this.deck.drawCard(p.getCard().getId()).get(),p.getDest());
                this.unselectedHand();
                this.unSelectField();

            } else if(o instanceof MoveFromFieldPacket){
                MoveFromFieldPacket p = (MoveFromFieldPacket) o;
                this.selectedFieldCard = p.getStart();
                this.moveFromFieldToField(p.getDest());
                this.unselectedHand();
                this.unSelectField();
            }
        }
        },"Player"+this.player).start();
    }
    private boolean isValid(Pair<Integer, Integer> pos, State state) {
        if(state.equals(State.HAND)){
            return (this.field.isFree(pos) &&
                    this.hand.getHand().get(this.selectedHandCard).isPresent() &&
                    this.hand.getHand().get(this.selectedHandCard).get().getMovementManager().canStartOn(pos, this.field.getSize()));
        }else if(state.equals(State.FIELD)){
            return true;
        }
        return false;
    }
    public void setController(GameController view) {
        System.out.println("Controller set"+view);
        this.controller = view;
    }
}
