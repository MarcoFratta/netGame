package core;

import cards.*;
import packets.*;
import rules.GameRules;

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
    private List<NetPlayer> turnPlayer;
    private int playersReady;
    private boolean isOver = false;

    public ServerLogic(GameRules gameRules, List<NetPlayer> list ){
        this.gameRules = gameRules;
        this.playersReady = 0;
        this.turnPlayer = new ArrayList<>();

        if(list.size() < gameRules.getMinPLayers() || list.size() > gameRules.getMaxPLayers()){
            throw new IllegalArgumentException();
        }
        System.out.println("Game rules ->"+gameRules);
        System.out.println("Players number ->"+list.size());
        this.turnPlayer.add(0,(NetPlayer) this.gameRules.getFirstPlayer().apply(list));
        List<NetPlayer> tmp = list.stream()
                .filter(p->!p.equals(turnPlayer.get(0))).collect(Collectors.toList());
        Collections.shuffle(tmp);
        this.turnPlayer.addAll(1,tmp);
        this.players = list.stream().
                collect(Collectors.toMap(a->a , a-> new HandImpl(gameRules.getHandSize())));

        System.out.println("Turn player ->"+turnPlayer);

        createDeck();
        createField();
        fillHands();
    }

    private void createField() {
        this.field = new FieldFactoryImpl().emptyField(gameRules.getFieldSize());
    }

    private void createDeck() {
        this.deck = new DeckBuilder()
                .path(gameRules.getDeckPath())
                .size(gameRules.getDeckSize())
                .shuffled()
                .build();
    }

    private void fillHands() {
        this.players.forEach((key, value) -> Stream.iterate(0, a -> a + 1)
                .limit(gameRules.getHandSize())
                .forEach(a -> draw(key)));

    }

    @Override
    public void startGame() {
        sendInfo();
        if(!waitForAllReady() || playersReady != players.size()){
            System.out.println("Ready error >"+playersReady);
            return;
        }
        
        sendHands();
        gameLoop();

    }

    private void gameLoop() {
        while (!isOver){
            System.out.println("Sneidng turn to ->"+turnPlayer.get(0).toString());
            sendToPlayer(Objects.requireNonNull(turnPlayer.get(0)),new TurnPacket());
            if(!turnPlayer.isEmpty()) {
                Object packet = turnPlayer.get(0).getComunicator().receive();
                System.out.println("Server Packet received from " + turnPlayer.get(0)+ "->" + packet);
                if (packet instanceof MoveFromFieldPacket) {
                    MoveFromFieldPacket p = (MoveFromFieldPacket) packet;
                    moveCardFromField(p);
                } else if (packet instanceof MoveFromHandPacket) {
                    MoveFromHandPacket p = (MoveFromHandPacket) packet;
                    moveCardFromHand(p);
                    if(!field.isFree(p.getDestPos())) {
                        packet = new AddedFromHandPacket(field.getCard(p.getDestPos()).get(), p.getDestPos());
                    }
                } else if (packet instanceof ErrorPacket) {
                    System.out.println(((ErrorPacket) packet).getMessage());
                    break;
                }
                System.out.println("t-Player before -> "+turnPlayer);
                sendToAllExceptOne(turnPlayer.get(0), packet);
                NetPlayer first = turnPlayer.remove(0);
                turnPlayer.add(players.size()-1,first);
                System.out.println("t-Player after -> "+turnPlayer);
            }

        }
    }

    private void sendToAllExceptOne(NetPlayer player, Object packet) {
        players.keySet().stream().filter(p->!p.equals(player))
                .forEach(p->sendToPlayer(p,packet));
    }

    private void moveCardFromHand(MoveFromHandPacket packet) {
        this.field.addCard(packet.getDestPos(),players.get(turnPlayer.get(0))
                .removeCard(packet.getSelectedHandCard()));
        if(deck.getLeftCardSize() > 0){
            this.players.get(turnPlayer.get(0)).getHand().add(deck.drawCard().get());
        }
    }

    private void moveCardFromField(MoveFromFieldPacket packet) {
        field.moveCard(field.getCard(packet.getStart()).get(),packet.getDest());
    }


    private void sendHands() {
        sendToAll(p-> new HandPacket(players.get(p).getHand()));
    }

    private boolean waitForAllReady() {
        ExecutorService es = Executors.newCachedThreadPool();
        boolean finished = false;
        for (NetPlayer p:players.keySet()) {
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
        players.keySet().forEach( p ->{
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
        sendToAll(p -> new GameInfoPacket(
                    sendingPlayers,
                    deck.getDeckType(),
                    gameRules.getDeckSize(),
                    gameRules.getHandSize(),
                    gameRules.getFieldSize(),
                    p.getId()));
        }


    private void draw(NetPlayer p){
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
