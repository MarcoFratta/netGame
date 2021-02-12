package views;


import cards.Card;
import cards.CardImpl;
import core.LocalLogic;
import core.Logic;
import core.Pair;
import core.Result;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import packets.GameInfoPacket;

import java.util.*;
import java.util.stream.IntStream;

public class GameControllerImpl implements GameController {

    public static final int SIZE = 1000;

    @FXML
    public BorderPane grid;

    private Map<CardBox, Pair<Integer,Integer>> map;
    private LocalLogic logic;
    private List<CardBox> handCards;
    private int selectedPos = -1;
    private boolean canPlay = false;

    @Override
    public void create(GameInfoPacket g, Logic logic){
        GridPane fieldGrid = new GridFactory().simpleSquareGrid(SIZE);
        this.logic = (LocalLogic) logic;
        double boxSize = SIZE/ g.getFieldSize();



        EventHandler<MouseEvent> fieldHandler = cell ->{
            if(canPlay) {
                CardBox clicked = (CardBox) cell.getSource();
                 this.logic.fieldTick(map.get(clicked));
                System.out.println("pos->" + map.get(clicked));
            }
        };

        EventHandler<MouseEvent> handHandler = cell ->{
            if(canPlay) {
                CardBox clicked = (CardBox) cell.getSource();
                System.out.println("pos->" + handCards.indexOf(clicked));
                int pos = this.handCards.indexOf(clicked);
                if (pos == selectedPos) {
                    selectedPos = -1;
                } else {
                    selectedPos = pos;
                    ((LocalLogic) logic).handTick(selectedPos);

                }
                selectHand();
                System.out.println("pos->" + handCards.indexOf(clicked));
            }
        };


        System.out.println("FIeld size->"+g.getFieldSize());
        map = new HashMap<>(g.getFieldSize()*g.getFieldSize());
        IntStream.range(0,g.getFieldSize())
                .forEach(i -> IntStream.range(0,g.getFieldSize())
                        .forEach(j ->{
                            CardBox c = new CardBox(boxSize);
                            fieldGrid.add(c,j,i);
                            c.setOnMouseClicked(fieldHandler);
                            map.put(c,new Pair<>(j,i));
                        }));
        this.grid.setCenter(fieldGrid);

        GridPane handGrid = new GridFactory().handGrid(g.getHandSize());
        this.handCards = new ArrayList<>(g.getHandSize());
        IntStream.range(0,g.getHandSize()).forEach(a->{
            CardBox c = new CardBox(boxSize);
            handGrid.add(c,a,0);
            c.setOnMouseClicked(handHandler);
            handCards.add(a,c);
        });

        this.grid.setBottom(handGrid);
    }

    private void selectHand() {
        this.handCards.forEach(CardBox::unselect);
        if(selectedPos != -1){
            this.handCards.get(selectedPos).select();
        }
    }

    @Override
    public void addHandCard(Card c, int position){
        Platform.runLater(()->this.handCards.get(position).setImage(((CardImpl)c).getImage()));
    }

    @Override
    public void addCardToField(Card card, Pair<Integer,Integer> pos) {
        Platform.runLater(()->{
            System.out.println("Adding card to"+pos);
         map.keySet().stream().filter(c -> map.get(c).equals(pos))
                .findAny().get().setImage(((CardImpl) card).getImage());
        });
    }

    @Override
    public void removeCardFromHand(int position){
        Platform.runLater(()->this.handCards.get(position).clearImage());
    }

    @Override
    public void close(Result result){
        String message = "lose";
        if(result.equals(Result.WIN)){
            message = "win";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Match is over");
        alert.setHeaderText("You "+message);
        alert.setContentText("exit:");
        alert.showAndWait();

        Stage stage = (Stage) grid.getScene().getWindow();
        stage.close();

    }

    @Override
    public void selectCells(List<Pair<Integer, Integer>> cells){
        Platform.runLater(()->this.map.keySet().stream()
                .filter(b -> cells.contains(map.get(b)))
                .forEach(CardBox::select));
    }
    @Override
    public void clearSelections(){
        Platform.runLater(()->this.map.keySet().forEach(CardBox::unselect));
    }

    @Override
    public void setCanPlay(Boolean b){
        this.canPlay = b;
    }

    @Override
    public void removeCardFromField(Pair<Integer, Integer> pos) {

    }

}
