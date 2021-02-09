package net;

import core.GameManager;
import core.LocalPlayer;
import core.Player;
import rules.GameRules;
import rules.SimpleGameRules;
import views.Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MenuManager{

    private Controller controller;
    private GameServer server;
    private List<Player> players;


    public MenuManager(Controller controller) {
        this.controller = controller;
        this.players = new ArrayList<>();
    }

    public void host(){
        this.players.clear();
        System.out.println("Host  ->"+this.players.toString());
        try {
            Player player = new LocalPlayer("Local User",InetAddress.getLocalHost(),GameServer.localPort);
            this.players.add(player);
        } catch (UnknownHostException e) {
            this.controller.notifyError("Local player error");
        }

        try {
            this.server = new GameServer(this);
            this.server.start();
            this.controller.serverStarted();
        }catch (Exception e){
            this.controller.notifyError("Can't start the server");
        }
    }

    public void join(String address){
        GameClient gameClient;
        this.players.clear();
        System.out.println("Join  ->"+this.players.toString());
        try {
            gameClient = new GameClient(InetAddress.getByName(address),GameServer.port,this);
            Player player = new LocalPlayer("Local User",InetAddress.getLocalHost(),GameServer.localPort);
            this.players.add(player);
        } catch (UnknownHostException e) {
            this.controller.notifyError("Not a valid input");
            return;
        }
        gameClient.start();
    }


    public void stopHosting() {
        this.server.stopHost();
    }


    public void connected(InetAddress address,int port) {
        System.out.println("Connesso  ->"+address);
        Player p = new LocalPlayer("GuestPlayer",address,port);
        players.add(p);
        GameRules gameRules = new SimpleGameRules(getClass().getResource("/cards_file/napoletane.json").getFile());
        GameManager gameManager = new GameManager(gameRules);
        this.players.forEach(gameManager::addPLayer);
        this.players.forEach(System.out::println);
        gameManager.startGame();
    }
}
