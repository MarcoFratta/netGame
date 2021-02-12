package views;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.ClientManager;
import net.HostManager;
import net.Manager;
import net.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            ((Runnable)() ->{
                manager=new ClientManager(this);
                manager.action(address);
            }).run();

        });
    }

    public void host(ActionEvent actionEvent) {
        if(!hosting){
            hosting = true;
            manager = new HostManager(this);
            manager.action(null);
            this.hostButton.setText("Hosting...\n(click to stop hosting)");
        }else {
            hosting = false;
            manager.stopAction();
            this.hostButton.setText("Host");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ipBox.setText(Utilities.getIp());
        notifyClientsUpdate(0);
    }

    public void notifyError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Connection error");
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void notifyClientsUpdate(int size) {
        Platform.runLater(() -> numPlayerBox.setText(PLAYERS_MESSAGE+size));
    }

    public void notifyServerStarted() {
    }
}
