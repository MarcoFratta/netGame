package net;

import core.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import rules.SimpleGameRules;
import views.MenuController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HostManager implements Manager{


    public static final String SERVER_ERROR = "Can't start the server";
    private final MenuController view ;
    private GameServer server;

    public HostManager(MenuController view) {
        this.view = view;
    }

    @Override
    public void action(String... args) {
        System.out.println("> Starting server.");
        try {
            this.server = new GameServer(this, Integer.parseInt(args[0])); // CHANGE TO ARGS
            this.server.start();
            this.view.notifyServerStarted();
        } catch (Exception e) {
            this.view.notifyError(SERVER_ERROR);
        }
    }

    @Override
    public void stopAction() {
        this.server.stopHost();
    }

    public void playerConnected(int size){
        this.view.notifyClientsUpdate(size);
    }
    public void playerDisconnected(int size){
        this.view.notifyClientsUpdate(size);
    }

    public void startMatch(Map<Socket, Pair<ObjectInputStream,
            ObjectOutputStream>> map) {

        final Integer[] counter = {0};
        List<NetPlayer> players =  map.keySet().stream()
                .map(a -> new NetPlayer(counter[0]++,"name",a, new ObjectComunicator(map.get(a).getX(),map.get(a).getY())))
                .collect(Collectors.toList());

        String path = this.getClass().getResource("/cards_file/napoletane.json").getFile();
        Logic logic = new ServerLogic(new SimpleGameRules(path),players);
        logic.startGame();


    }


}
