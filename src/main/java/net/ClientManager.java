package net;

import core.LocalLogic;
import core.LocalPlayer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import packets.GameInfoPacket;
import views.GameController;
import views.MenuController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.*;

public class ClientManager implements Manager {
    public static final String LAYOUT_PATH = "/layouts/game.fxml";
    private final MenuController view;
    private GameClient client;
    private List<LocalPlayer> players;

    public ClientManager(MenuController view) {
        this.view = view;
    }

    @Override
    public void action(String args) {
        try {
            this.client = new GameClient(InetAddress.getByName(args), this);
            this.client.start();
        } catch (UnknownHostException e) {
            this.view.notifyError("No match found");
        }
    }

    @Override
    public void stopAction() {

    }

    public void startMatch(GameInfoPacket p, ObjectInputStream i, ObjectOutputStream o) {
        this.players = p.getPlayers();
        Comunicator server = new ObjectComunicator(i, o);
        LocalLogic logic = new LocalLogic(p, server);
        new Thread(()-> {
            final FutureTask<GameController> query = new FutureTask<>(new Callable<>() {
                @Override
                public GameController call() {
                    Stage stage = new Stage();
                    FXMLLoader loader;
                    loader = new FXMLLoader(this.getClass().getResource(LAYOUT_PATH));
                    stage.setTitle("Game");
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GameController view = loader.getController();
                    view.create(p, logic);
                    Platform.runLater(stage::show);
                    return view;
                }
            });
            Platform.runLater(query);
            try {
                logic.setController(query.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            logic.startGame();
        }).start();


    }
}
