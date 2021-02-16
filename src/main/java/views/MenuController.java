package views;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.ClientManager;
import net.HostManager;
import net.Manager;
import net.Utilities;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private static final String PLAYERS_MESSAGE = "PLayers connected:";
    @FXML
    public Button hostButton;
    @FXML
    public Label numPlayerBox;
    @FXML
    private Label ipBox;
    private Manager manager;
    private boolean hosting = false;


    public void join(ActionEvent actionEvent) {
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
                    m.action(address, n);
                }, "Client manager").start();
            });


        });
    }

    public void host(ActionEvent actionEvent) {
        if(!this.hosting) {
            TextInputDialog dialog = new TextInputDialog("2");
            dialog.setTitle("Host a game");
            dialog.setHeaderText("Multiplayer");
            dialog.setContentText("Insert player number:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(number -> {
                try {
                    this.hosting = true;
                    this.manager = new HostManager(this);
                    this.manager.action(number);
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
}
