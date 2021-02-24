package net;

import cards.DeckLoader;
import core.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import rules.GameRules;
import rules.SimpleGameRules;
import views.MenuController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HostManager implements Manager {


    public static final String SERVER_ERROR = "Can't start the server";
    private final MenuController view;
    private List<String> decks;
    private GameServer server;
    private String path;
    private final DeckLoader deckLoader;
    private GameRules gameRules;

    public HostManager(MenuController view) {
        this.view = view;
        this.deckLoader = new DeckLoader();
        try {
            this.decks = this.deckLoader.loadDecks();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        this.view.setDecks(List.copyOf(this.decks));
    }

    @Override
    public void action(Object... args) {
        System.out.println("> Starting server.");
        this.gameRules = (GameRules) args[0];
        try {
            this.server = new GameServer(this, this.gameRules.getMaxPLayers()); // CHANGE TO ARGS
            //this.path = this.deckLoader.getDefPath()+"\\"+args[1]+".json";
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
            this.view.notifyError(SERVER_ERROR);
        }
    }

    @Override
    public void stopAction() {
        this.server.stopHost();
    }

    public void playerConnected(int size){
        System.out.println("Connected");
        this.view.notifyClientsUpdate(size);
    }
    public void playerDisconnected(int size){
        this.view.notifyClientsUpdate(size);
    }

    public void startMatch(Map<Socket, Pair<ObjectInputStream,
            ObjectOutputStream>> map) {
        System.out.println("Game rules -> " + this.gameRules.toString());

        final Integer[] counter = {0};
        List<NetPlayer> players = map.keySet().stream()
                .map(a -> new NetPlayer(counter[0]++, "name", a, new ObjectComunicator(map.get(a).getX(), map.get(a).getY())))
                .collect(Collectors.toList());


        Logic logic = new ServerLogic(this.gameRules, players);
        logic.startGame();
    }

}
