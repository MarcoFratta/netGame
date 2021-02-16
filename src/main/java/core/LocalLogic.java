package core;

import cards.*;
import net.Comunicator;
import packets.*;
import rules.Result;
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
        System.out.println("Sending ready message " + this.player);
        this.server.send(new ReadyForHandPacket(this.player.getId()));
        this.waitForHand();
        this.showHand();
        this.waitForTurn();
    }

    public boolean canEat(Card card, Optional<Card> p) {
        if (p.isEmpty()) {
            return true;
        }
        return p.get().canEat() && (!card.getSeed().equals(p.get().getSeed()));
    }

    private void showHand() {
        for (int i = 0; i < this.hand.getHand().size(); i++) {
            this.controller.addHandCard(this.hand.getHand().get(i), i);
        }

    }

    private void waitForHand() {
        System.out.println("Waiting for hand " + this.player.toString());
        Object p = this.server.receive();
        if(p instanceof HandPacket){
            List<Card> cards = ((HandPacket) p).getCards();
            this.addCardsToHand(cards);
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
            this.unSelectField();
            this.controller.clearHandSelections();
        }
    }
    public void unselectedHand(){
        this.selectedHandCard = NO_HAND_SELECTED;
    }

    public void unSelectField() {
        this.selectedFieldCard = null;
    }

    public void fieldTick(Pair<Integer, Integer> dest) {
        //System.out.println("BEOFRE FIELD CLICK -> "+this.selectedHandCard+" ->"+this.selectedFieldCard);
        if (this.selectedHandCard != NO_HAND_SELECTED) { // want to move from hand to field
            if (this.isValid(dest, State.HAND)) {
                Card c = this.hand.removeCard(this.selectedHandCard).get();
                this.server.send(new MoveFromHandPacket(c, dest));
                this.moveFromHandToField(c, dest);
                this.adjustView();
            }
        } else if (this.selectedFieldCard != null) {
            if (this.selectedFieldCard.equals(dest)) {
                this.unSelectField();
                this.controller.clearFieldSelections();
            } else if (this.isValid(dest, State.FIELD)) { // want to move from field to field
                //System.out.println("Moving from field to field >"+ this.player);
                this.server.send(new MoveFromFieldPacket(new Pair<>(this.selectedFieldCard), dest));
                this.moveFromFieldToField(dest);
                this.adjustView();
            }
        } else if (!this.field.isFree(dest)) {
            this.unselectedHand(); // start field card selected
            this.controller.clearHandSelections();
            this.selectedFieldCard = dest;
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
                        .stream()
                        .filter(this.field::isFree)
                        .collect(Collectors.toList());
            } else if(state.equals(State.FIELD)) {
                List<Pair<Integer, Integer>> selectedCells = new ArrayList<>();
                selectedCells.add(this.selectedFieldCard);
                selectedCells.addAll(this.field.getCard(this.selectedFieldCard).get()
                        .getMovementManager().getDestinations(state, this.selectedFieldCard, this.field.getSize()));
                return selectedCells.stream()
                        .filter(p -> this.canEat(this.field.getCard(this.selectedFieldCard).get(),
                                this.field.getCard(p))).collect(Collectors.toList());
            }
        }catch (Exception e){
            e.printStackTrace();
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

            } else if (o instanceof MoveFromFieldPacket) {
                MoveFromFieldPacket p = (MoveFromFieldPacket) o;
                this.selectedFieldCard = p.getStart();
                this.moveFromFieldToField(p.getDest());
                this.unselectedHand();
                this.unSelectField();
            } else if (o instanceof HandPacket) {
                this.addCardsToHand(((HandPacket) o).getCards());
                this.showHand();
            } else if (o instanceof GameOverPacket) {
                endMatch(((GameOverPacket) o).getResult());
            }
        }
        }, "Player" + this.player).start();
    }

    private void endMatch(Result result) {
        this.controller.showResultAndExit(result);
    }

    private void addCardsToHand(List<Card> cards) {
        cards.forEach(c -> this.hand.setCard(this.deck.drawCard(c.getId()).get()));
    }


    private boolean isValid(Pair<Integer, Integer> dest, State state) {
        if (state.equals(State.HAND)) {
            return (this.field.isFree(dest) &&
                    this.hand.getHand().get(this.selectedHandCard).isPresent() &&
                    this.hand.getHand().get(this.selectedHandCard).get()
                            .getMovementManager().canStartOn(dest, this.field.getSize()));
        } else if (state.equals(State.FIELD)) {
            return this.field.getCard(this.selectedFieldCard).get()
                    .getMovementManager().canMoveTo(this.selectedFieldCard, dest, this.field.getSize()) &&
                    this.canEat(this.field.getCard(this.selectedFieldCard).get(), this.field.getCard(dest));
        }
        return false;
    }
    public void setController(GameController view) {
        System.out.println("Controller set"+view);
        this.controller = view;
    }
}
