package views;


import cards.DeckLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.ClientManager;
import net.HostManager;
import net.Manager;
import net.Utilities;
import rules.SettingsManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuController implements Initializable {

    private static final String PLAYERS_MESSAGE = "Players connected:";
    private static final String CONNECTED_MESSAGE = "Connected";


    @FXML
    public Button hostButton;
    @FXML
    public Label numPlayerBox;
    @FXML
    public ComboBox<String> deckList;
    @FXML
    private Label ipBox;

    private Manager manager;
    private boolean hosting = false;
    private SettingsManager settingsManager;

    public void join(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Localhost");
        dialog.setTitle("Connect to a host");
        dialog.setHeaderText("IP address");
        dialog.setContentText("Insert server address:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(address -> {
            TextInputDialog d2 = new TextInputDialog("Npc");
            d2.setTitle("Multiplayer online");
            d2.setHeaderText("Player name");
            d2.setContentText("Insert your name:");
            Optional<String> name = d2.showAndWait();
            name.ifPresent(n -> {
                new Thread(() -> {
                    Manager m = new ClientManager(this, this.deckList.getItems());
                    m.action(address, n);
                }, "Client manager").start();
            });
        });
    }

    public void host(ActionEvent actionEvent) {
        if (this.deckList.getSelectionModel().isEmpty()) {
            return;
        }
        if (!this.hosting) {
            try {
                this.hosting = true;
                this.manager = new HostManager(this);
                this.manager.action(this.settingsManager.getSettings());
                this.hostButton.setText("Hosting...\n(click to stop hosting)");
            } catch (Exception e) {
                e.printStackTrace();
                this.notifyError("Invalid number");
            }
        }else {
            this.hosting = false;
            this.manager.stopAction();
            this.hostButton.setText("Host");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ipBox.setText(Utilities.getIp());
        this.notifyClientsUpdate(0);
        this.manager = new HostManager(this);
        this.settingsManager = new SettingsManager();
    }

    public void notifyError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Connection error");
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void notifyClientsUpdate(int size) {
        Platform.runLater(() -> this.numPlayerBox.setText(PLAYERS_MESSAGE + size));
    }

    public void notifyConnection() {
        Platform.runLater(() -> this.numPlayerBox.setText(CONNECTED_MESSAGE));
    }

    public Window getScene() {
        return this.hostButton.getScene().getWindow();
    }

    public void setDecks(List<String> copyOf) {
        Platform.runLater(() -> {
            this.deckList.getItems().setAll(copyOf);
            this.deckList.getSelectionModel().select(0);
        });
    }

    public void openSettings(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/layouts/settings.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Hello World");
        settingsStage.setScene(new Scene(Objects.requireNonNull(root)));
        settingsStage.show();
        SettingsController settingsController = loader.getController();
        settingsController.setSettingsManager(this.settingsManager);
    }

    public void changeDeck(ActionEvent actionEvent) {
        System.out.println("Deck selected");
        this.settingsManager.setPath(this.deckList.getValue());
    }
}
