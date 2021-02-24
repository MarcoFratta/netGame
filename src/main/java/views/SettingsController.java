package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import rules.SettingsManager;


public class SettingsController {
    public TextField playersNum;
    public TextField handSize;
    public TextField deckSize;
    public TextField fieldSize;
    private SettingsManager manager;


    public void setSettingsManager(SettingsManager s) {
        this.playersNum.getScene().getWindow().setOnCloseRequest(a -> {
            //System.out.println("Chiudendo");
            this.setPlayersNum(null);
            this.setFieldSize(null);
            this.setHandSize(null);
            this.setDeckSize(null);
        });
        //System.out.println("Pnum->"+s.getPlayersNum());
        this.manager = s;
        this.playersNum.setText(String.valueOf(s.getPlayersNum()));
        this.handSize.setText(String.valueOf(s.getHandCardsNum()));
        this.deckSize.setText(String.valueOf(s.getDeckSize()));
        this.fieldSize.setText(String.valueOf(s.getFieldSize()));
    }

    @FXML
    public void setPlayersNum(ActionEvent e) {
        try {
            this.manager.setPlayersNum(Integer.parseInt(this.playersNum.getText()));
        } catch (Exception ex) {

        }
    }

    @FXML
    public void setHandSize(ActionEvent e) {
        try {
            this.manager.setHandCardsNum(Integer.parseInt(this.handSize.getText()));
        } catch (Exception ex) {

        }
    }

    @FXML
    public void setDeckSize(ActionEvent e) {
        try {
            this.manager.setDeckSize(Integer.parseInt(this.deckSize.getText()));
        } catch (Exception ex) {

        }
    }

    @FXML
    public void setFieldSize(ActionEvent e) {
        try {
            this.manager.setFieldSize(Integer.parseInt(this.fieldSize.getText()));
        } catch (Exception ex) {

        }
    }
}
