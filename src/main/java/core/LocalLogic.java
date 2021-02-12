package core;

import cards.*;
import net.Comunicator;
import packets.*;
import views.GameController;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LocalLogic implements Logic {

    private enum State{
        HAND,FIELD
    }


    public static final String CARDS_PACKAGE = "/cards_file/";
    public static final String FILE_TYPE = ".json";
    private final List<? extends Player> players;
    private final Hand hand;
    private final Comunicator server;
    private final Deck deck;
    private final Field field;
    private GameController controller;
    private boolean isMyTurn = false;
    private Player player;
    private Pair<Integer,Integer> selectedFieldCard;
    private int selectedHandCard;


    public LocalLogic(GameInfoPacket packet,Comunicator server) {
        this.players = packet.getPlayers();
        this.server = server;
        try {
            this.player = players.stream().filter(p -> p.getId() == packet.getDestPlayerId())
                    .findAny().get();
        }catch (NoSuchElementException e){
            server.send(new ErrorPacket("id error"));
        }
        try{
            String path = getClass().getResource(CARDS_PACKAGE +packet.getDeckType()+ FILE_TYPE).getFile();
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
        System.out.println("Sending ready message "+player);
        server.send(new ReadyForHandPacket(this.player.getId()));
        waitForHand();
        showHand();
        waitForTurn();
    }

    private void showHand() {
        for(int i = 0 ; i < hand.getHand().size();i++){
            controller.addHandCard(hand.getHand().get(i),i);
        }

    }

    private void waitForHand() {
        System.out.println("Waiting for hand "+player.toString());
        Object p = server.receive();
        if(p instanceof HandPacket){
            List<Card> cards = ((HandPacket) p).getCards();
            cards.forEach(c ->{
                try {
                    Card card = deck.drawCard(c.getSeed(), c.getNumber()).get();
                    hand.addCard(card);
                }catch (NoSuchElementException e){
                    System.out.println("starting hand error");
                }
            });
        }
    }

    public void handTick(int pos) {
        selectedHandCard = pos;
        controller.selectCells(getValidCells(State.HAND));
    }

    public void unselectedHand(){
        selectedHandCard = -1;
    }
    public void unSelectField(){
        selectedFieldCard = null;
    }

    public void fieldTick(Pair<Integer,Integer> pos){
        if(selectedHandCard!=-1){ // want to move from hand to field
            if(isValid(pos,State.HAND)){
                field.addCard(pos,hand.getHand().get(selectedHandCard));
                System.out.println("Sending -> "+player+"->hand->"+pos);
                server.send(new MoveFromHandPacket(selectedHandCard,pos));
                moveCardFromHand(pos);
                adjustView();
            }
        }else if(selectedFieldCard!=null){ // want to move from field to field
            if(isValid(pos,State.FIELD)){
                System.out.println("Sending -> "+player+"->field->"+pos);
                server.send(new MoveFromFieldPacket(selectedFieldCard,pos));
                Optional<Card> c = field.getCard(selectedFieldCard);
                c.ifPresent(card -> field.moveCard(card, pos));
                moveCardOnField(pos);
                adjustView();

            }
        } else {
                selectedFieldCard = pos;
                controller.selectCells(getValidCells(State.HAND));
        }
    }

    private void adjustView() {
        unSelectField();
        unselectedHand();
        isMyTurn = false;
        controller.setCanPlay(false);
        waitForTurn();

    }

    private void moveCardFromHand(Pair<Integer, Integer> pos) {
        controller.addCardToField(hand.getHand().get(selectedHandCard), pos);
        controller.removeCardFromHand(selectedHandCard);
    }

    private void moveCardOnField(Pair<Integer, Integer> pos) {
        controller.removeCardFromField(pos);
        controller.removeCardFromField(selectedFieldCard);
        controller.addCardToField(field.getCard(selectedFieldCard).get(),selectedFieldCard);
    }


    private List<Pair<Integer, Integer>> getValidCells(State hand) {
        //field.moveCard()
        return Collections.emptyList();
    }

    private void waitForTurn() {
        new Thread(()->{
        while(!isMyTurn) {
            System.out.println("Waiting turn... "+player);
            Object o = server.receive();
            System.out.println("Packet received- "+player+" p->"+o);
            if (o instanceof TurnPacket) {
                this.isMyTurn = true;
                controller.setCanPlay(true);
            } else if (o instanceof AddedFromHandPacket) {
                AddedFromHandPacket p = (AddedFromHandPacket) o;
                controller.addCardToField(deck.drawCard(p.getCard().getSeed(),p.getCard().getNumber()).get(), p.getDest());
                field.addCard(p.getDest(),p.getCard());

            } else if(o instanceof MoveFromFieldPacket){
                MoveFromFieldPacket p = (MoveFromFieldPacket) o;
                controller.removeCardFromField(p.getStart());
                controller.removeCardFromField(p.getDest());
                field.moveCard(field.getCard(p.getStart()).get(),p.getDest());
            }
        }
        }).start();
    }

    private boolean isValid(Pair<Integer, Integer> pos, State state) {
        if(state.equals(State.HAND)){
            return (field.isFree(pos) &&
                    hand.getHand().get(selectedHandCard).getMovementManager().canStartOn(pos, field.getSize()));
        }else if(state.equals(State.FIELD)){

        }
        return false;
    }

    public void setController(GameController view) {
        System.out.println("Controller set"+view);
        this.controller = view;
    }
}
