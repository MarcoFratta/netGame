package views;

import core.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import net.MenuManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public Button hostButton;
    private MenuManager manager;
    private boolean hosting = false;


    public void join(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Connect");
        dialog.setTitle("Connect to a host");
        dialog.setHeaderText("IP address");
        dialog.setContentText("Insert server address:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(address ->  manager.join(address));

    }

    public void host(ActionEvent actionEvent) {
        if(!hosting){
            hosting = true;
            manager.host();
            this.hostButton.setText("Hosting...\n(click to stop hosting)");
        }else {
            hosting = false;
            manager.stopHosting();
            this.hostButton.setText("Host");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new MenuManager(this);
    }

    public void notifyError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Connection error");
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void serverStarted() {
        
    }
}
