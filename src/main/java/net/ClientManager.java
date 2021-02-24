package net;

import cards.DeckLoader;
import core.LocalLogic;
import core.LocalPlayer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    private List<String> decks;
    private GameClient client;
    private List<LocalPlayer> players;
    private String name;
    private String path;

    public ClientManager(MenuController view, List<String> decks) {
        this.view = view;
        this.decks = decks;
    }

    @Override
    public void action(Object... args) {
        this.name = (String) args[1];
        try {
            this.client = new GameClient(InetAddress.getByName((String) args[0]), this);
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
        if (!this.decks.contains(p.getDeckType())) {
            this.client.stopServer();
            return;
        }
        DeckLoader deckLoader = new DeckLoader();
        this.path = deckLoader.getJsonFilePath(p.getDeckType());
        Comunicator server = new ObjectComunicator(i, o);
        LocalLogic logic = new LocalLogic(p, server, this.path);
        new Thread(() -> {
            final FutureTask<GameController> query = new FutureTask<>(new Callable<>() {
                @Override
                public GameController call() {
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(App.class.getResourceAsStream("/guru.png")));
                    FXMLLoader loader;
                    loader = new FXMLLoader(this.getClass().getResource(LAYOUT_PATH));
                    stage.setTitle("Game");
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GameController controller = loader.getController();
                    controller.create(p, logic);
                    stage.setOnCloseRequest(e -> {
                        Platform.exit();
                        System.exit(0);
                    });
                    Platform.runLater(stage::show);
                    Stage menu = (Stage) ClientManager.this.view.getScene();
                    menu.close();
                    return controller;
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
