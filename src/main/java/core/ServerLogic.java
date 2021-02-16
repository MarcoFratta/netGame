package core;

import cards.*;
import packets.*;
import rules.GameRules;
import rules.Result;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerLogic implements Logic {

    private  Field field;
    private final Map<NetPlayer, Hand> players;
    private  Deck deck;
    private final GameRules gameRules;
    private final List<NetPlayer> turnPlayer;
    private int playersReady;
    private final boolean isOver = false;
    private final List<Card> removedCards;

    public ServerLogic(GameRules gameRules, List<NetPlayer> list ){
        this.gameRules = gameRules;
        this.playersReady = 0;
        this.turnPlayer = new ArrayList<>();
        this.removedCards = new ArrayList<>();

        if(list.size() < gameRules.getMinPLayers() || list.size() > gameRules.getMaxPLayers()){
            throw new IllegalArgumentException();
        }
        System.out.println("Game rules ->"+gameRules);
        System.out.println("Players number ->"+list.size());
        this.turnPlayer.add(0,(NetPlayer) this.gameRules.getFirstPlayer().apply(list));
        List<NetPlayer> tmp = list.stream()
                .filter(p->!p.equals(this.turnPlayer.get(0))).collect(Collectors.toList());
        Collections.shuffle(tmp);
        this.turnPlayer.addAll(1,tmp);
        this.players = list.stream().
                collect(Collectors.toMap(a->a , a-> new HandImpl(gameRules.getHandSize())));

       // System.out.println("Turn player ->"+turnPlayer);

        this.createDeck();
        this.createField();
        this.fillHands();
    }

    private void createField() {
        this.field = new FieldFactoryImpl().emptyField(this.gameRules.getFieldSize());
    }

    private void createDeck() {
        this.deck = new DeckBuilder()
                .path(this.gameRules.getDeckPath())
                .size(this.gameRules.getDeckSize())
                .shuffled()
                .build();
    }

    private void fillHands() {
        this.players.forEach((key, value) -> Stream.iterate(0, a -> a + 1)
                .limit(this.gameRules.getHandSize())
                .forEach(a -> this.randomDraw(key)));

    }

    @Override
    public void startGame() {
        this.sendInfo();
        if(!this.waitForAllReady() || this.playersReady != this.players.size()){
            System.out.println("Ready error >"+ this.playersReady);
            return;
        }

        this.sendHands();
        this.gameLoop();

    }

    private void gameLoop() {
        while (!this.isOver){
            System.out.println("Sending turn to ->"+ this.turnPlayer.get(0).toString());
            this.sendToPlayer(Objects.requireNonNull(this.turnPlayer.get(0)),new TurnPacket());
            if(!this.turnPlayer.isEmpty()) {
                Object packet = this.turnPlayer.get(0).getComunicator().receive();
                System.out.println("Server Packet received from " + this.turnPlayer.get(0)+ "->" + packet);
                if (packet instanceof MoveFromFieldPacket) {
                    MoveFromFieldPacket p = (MoveFromFieldPacket) packet;
                    this.moveCardFromField(p);
                } else if (packet instanceof MoveFromHandPacket) {
                    MoveFromHandPacket p = (MoveFromHandPacket) packet;
                    this.moveCardFromHand(p);
                    if (!this.field.isFree(p.getDestPos())) {
                        packet = new AddedFromHandPacket(this.field.getCard(p.getDestPos()).get(), p.getDestPos());
                    }
                    this.sendNewCard();
                } else if (packet instanceof ErrorPacket) {
                    System.out.println(((ErrorPacket) packet).getMessage());
                    break;
                }
                if (this.gameOver()) {
                    System.out.println("GAME OVER");
                    this.sendToAll(p -> p.equals(this.turnPlayer.get(0)) ?
                            new GameOverPacket(Result.WIN) :
                            new GameOverPacket(Result.LOSE));
                }
                System.out.println("Deck size -> " + this.deck.getLeftCardSize());
                this.sendToAllExceptOne(this.turnPlayer.get(0), packet);
                NetPlayer first = this.turnPlayer.remove(0);
                this.turnPlayer.add(this.players.size() - 1, first);
                //System.out.println("t-Player after -> "+turnPlayer);
            }

        }
    }

    private boolean gameOver() {
        return this.gameRules.getWinCondition().test(this.field);
    }

    private void sendNewCard() {
        Optional<Card> c = this.deck.drawCard();
        c.ifPresent(card -> {
            this.players.get(this.turnPlayer.get(0)).setCard(card);
            this.sendToPlayer(this.turnPlayer.get(0),
                    new HandPacket(List.of(card)));
        });

    }

    private void sendToAllExceptOne(NetPlayer player, Object packet) {
        System.out.println("Server sending " + packet + " to all except->" + player);
        this.players.keySet().stream().filter(p -> !p.equals(player))
                .forEach(p -> this.sendToPlayer(p, packet));
    }

    private void moveCardFromHand(MoveFromHandPacket packet) {
        Optional<Card> cardFromHand = this.players.get(this.turnPlayer.get(0))
                .removeCardFromId(packet.getSelectedHandCard().getId());
        System.out.println("Received hand card ->" + cardFromHand.get().getId());
        cardFromHand.ifPresent(c -> this.field.addCard(packet.getDestPos(), cardFromHand.get()));
    }


    private void moveCardFromField(MoveFromFieldPacket packet) {// cant be null
        Optional<Card> removed = this.field.moveCard(packet.getStart(), packet.getDest());
        removed.ifPresent(this.removedCards::add);
    }


    private void sendHands() {
        this.sendToAll(p -> new HandPacket(this.players.get(p).getHand()
                .stream()
                .map(Optional::get)
                .collect(Collectors.toList())));
    }

    private boolean waitForAllReady() {
        ExecutorService es = Executors.newCachedThreadPool();
        boolean finished = false;
        for (NetPlayer p: this.players.keySet()) {
            es.execute(() -> {
                System.out.println("Waiting for ready ->"+p);
                Object packet = p.getComunicator().receive();
                if(packet instanceof ReadyForHandPacket){
                    System.out.println("- ready ->"+p);
                    this.playersReady++;
                }else if(packet instanceof ErrorPacket){
                    System.out.println("player->"+p+" error-> "+((ErrorPacket) packet).getMessage());
                }
            });
        };
        try {
             es.shutdown();
             finished = es.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished ->"+finished);
        return finished;
    }

    public void sendToAll(Function<NetPlayer,Packet> fun){
        this.players.keySet().forEach(p ->{
            p.getComunicator().send(fun.apply(p));
        });
    }
    public void sendToPlayer(NetPlayer p, Object packet){
        p.getComunicator().send(packet);
    }


    private void sendInfo() {
        List<LocalPlayer> sendingPlayers = this.players.keySet()
                .stream()
                .map(player -> new LocalPlayer(player.getId(),player.getName()))
                .collect(Collectors.toList());
        System.out.println("Server sending game info");
        this.sendToAll(p -> new GameInfoPacket(
                    sendingPlayers,
                this.deck.getDeckType(),
                this.gameRules.getDeckSize(),
                this.gameRules.getHandSize(),
                this.gameRules.getFieldSize(),
                    p.getId()));
        }


    private void randomDraw(NetPlayer p){
        if(this.deck.getLeftCardSize() > 0)
        {
            Optional<Card> c = this.deck.drawCard();
            c.ifPresent(card -> this.players.get(p).setCard(card));
        }
    }
    public List<Hand> getHands(){
        return List.copyOf(this.players.values());
    }
    public Field getField(){
        return this.field;
    }
}
