package views;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.ClientManager;
import net.HostManager;
import net.Manager;
import net.Utilities;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private static final String PLAYERS_MESSAGE = "PLayers connected:";
    public static final String DECKS_FOLDER = ".Guru/Decks";
    @FXML
    public Button hostButton;
    @FXML
    public Label numPlayerBox;
    public Button pathLoader;
    @FXML
    private Label ipBox;
    private Manager manager;
    private boolean hosting = false;
    private String path = "";


    public void join(ActionEvent actionEvent) {
        if (this.path.isEmpty()) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog("Localhost");
        dialog.setTitle("Connect to a host");
        dialog.setHeaderText("IP address");
        dialog.setContentText("Insert server address:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(address -> {
            TextInputDialog d2 = new TextInputDialog("Npc");
            dialog.setTitle("Multiplayer online");
            dialog.setHeaderText("Player name");
            dialog.setContentText("Insert your name:");
            Optional<String> name = dialog.showAndWait();
            name.ifPresent(n -> {
                new Thread(() -> {
                    Manager m = new ClientManager(this);
                    m.action(address, n, this.path);
                }, "Client manager").start();
            });


        });
    }

    public void host(ActionEvent actionEvent) {
        if (this.path.isEmpty()) {
            return;
        }
        if (!this.hosting) {
            TextInputDialog dialog = new TextInputDialog("2");
            dialog.setTitle("Host a game");
            dialog.setHeaderText("Multiplayer");
            dialog.setContentText("Insert player number:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(number -> {
                try {
                    this.hosting = true;
                    this.manager = new HostManager(this);
                    this.manager.action(number, this.path);
                    this.hostButton.setText("Hosting...\n(click to stop hosting)");
                } catch (Exception e) {
                    this.notifyError("Invalid number");
                }
            });
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
        this.tryLoadDeck();
    }

    private void tryLoadDeck() {
        File folder = new File(System.getProperty("user.home"), DECKS_FOLDER);
        try {
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory() && fileEntry.getPath().endsWith(".json")) {
                    this.path = fileEntry.getPath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.path = "";
        }
        System.out.println("Loaded deck->" + this.path);
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

    public void notifyServerStarted() {
    }

    public Window getScene() {
        return this.hostButton.getScene().getWindow();
    }

    public void path(ActionEvent actionEvent) {
        File recordsDir = new File(System.getProperty("user.home"), DECKS_FOLDER);
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(recordsDir);


        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(this.getScene());
        if (file != null) {
            this.path = file.getPath();
        }

    }
}
