package views;


import cards.Card;
import cards.CardImpl;
import core.LocalLogic;
import core.Logic;
import core.Pair;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import rules.Result;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import packets.GameInfoPacket;

import java.util.*;
import java.util.stream.IntStream;

public class GameControllerImpl implements GameController {

    public static final int SIZE = 600;
    private static final double xRatio = 0.5;
    private static final double yRatio = 0.8;
    private static final double xOffsetRatio = 0.45;
    private static final double yOffsetRatio = 0.37;
    private static final String TURN_COLOR = "#e383fd";
    public static final double OPACITY = 0.2;
    public static final int SPACING = 20;

    @FXML
    public BorderPane grid;
    @FXML
    public Circle turnCircle;

    private Map<CardBox, Pair<Integer, Integer>> map;
    private LocalLogic logic;
    private List<CardBox> handCards;
    private boolean canPlay;
    private int turnCount;

    @Override
    public void create(GameInfoPacket g, Logic logic) {
        GridPane fieldGrid = new GridFactory().simpleSquareGrid(SIZE);
        this.logic = (LocalLogic) logic;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Stage scene = (Stage) this.grid.getScene().getWindow();
        scene.setWidth(screenBounds.getWidth() * xRatio);
        scene.setHeight(screenBounds.getHeight() * yRatio);
        scene.show();
        System.out.println("Scene size -> " + scene.getWidth() + "-" + scene.getHeight());
        double boxSize = this.computeFieldBoxSize(scene, g);


        EventHandler<MouseEvent> fieldHandler = cell -> {
            if (this.canPlay) {
                CardBox clicked = (CardBox) cell.getSource();
                this.logic.fieldTick(this.map.get(clicked));
            }
        };

        Stage stage = (Stage) this.grid.getScene().getWindow();
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            this.map.keySet().forEach(c -> c.fixSize(this.computeFieldBoxSize(stage, g)));
            this.handCards.forEach(c -> c.fixSize(this.computeHandBoxSize(stage, g)));
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);


        EventHandler<MouseEvent> handHandler = cell -> {
            if (this.canPlay) {
                CardBox clicked = (CardBox) cell.getSource();
                System.out.println("pos->" + this.handCards.indexOf(clicked));
                int pos = this.handCards.indexOf(clicked);
                ((LocalLogic) logic).handTick(pos);
                // System.out.println("pos->" + handCards.indexOf(clicked));
            }
        };


        System.out.println("Field size->" + g.getFieldSize());
        this.map = new HashMap<>(g.getFieldSize() * g.getFieldSize());
        IntStream.range(0, g.getFieldSize())
                .forEach(i -> IntStream.range(0, g.getFieldSize())
                        .forEach(j -> {
                            CardBox c = new CardBox(boxSize, j, i, g.getFieldSize());
                            fieldGrid.add(c, j, i);
                            c.setOnMouseClicked(fieldHandler);
                            this.map.put(c, new Pair<>(j, i));
                        }));
        this.turnCount = 0;
        this.grid.setCenter(fieldGrid);


        GridPane handGrid = new GridFactory().handGrid(g.getHandSize());
        this.handCards = new ArrayList<>(g.getHandSize());
        IntStream.range(0, g.getHandSize()).forEach(a -> {
            CardBox c = new CardBox(this.computeHandBoxSize(scene, g));
            handGrid.add(c, a, 0);
            c.setOnMouseClicked(handHandler);
            this.handCards.add(a, c);
        });

        HBox buttonsView = new HBox();
        buttonsView.setAlignment(Pos.CENTER);
        buttonsView.setSpacing(SPACING);

        Button hideButton = new Button("Hide");
        hideButton.setOnAction((a -> {
            this.map.keySet().forEach(CardBox::setHide);
        }));
        hideButton.getStyleClass().add("label");

        Button sortButton = new Button("Sort");
        sortButton.setOnAction((a -> {
            this.logic.sort();
        }));
        sortButton.getStyleClass().add("label");

        buttonsView.getChildren().addAll(sortButton, hideButton);

        VBox lower = new VBox();
        lower.setAlignment(Pos.CENTER);
        lower.setSpacing(SPACING);
        lower.getChildren().setAll(buttonsView, handGrid);

        this.grid.setBottom(lower);
        this.setCanPlay(false);
    }

    private double computeHandBoxSize(Stage scene, GameInfoPacket g) {
        return (scene.getWidth() - (scene.getWidth() * xOffsetRatio)) / g.getHandSize();
    }

    private double computeFieldBoxSize(Stage scene, GameInfoPacket g) {
        return (scene.getHeight() - (scene.getHeight() * yOffsetRatio)) / g.getFieldSize();
    }


    public void selectHand(int position) {
        this.handCards.forEach(b -> {
            b.unselect();
            b.disable();
        });
        this.handCards.get(position).enable();
        this.handCards.get(position).select();
    }

    @Override
    public void addHandCard(Optional<Card> card, int position) {
        Platform.runLater(() -> card.ifPresent(c -> {
            this.handCards.get(position).setImage(((CardImpl) c).getImage());
        }));
    }

    @Override
    public void addCardToField(Card card, Pair<Integer,Integer> pos) {
        Platform.runLater(()->{
           // System.out.println("Adding card to"+pos);
            this.map.keySet().stream().filter(c -> this.map.get(c).equals(pos))
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

        Stage stage = (Stage) this.grid.getScene().getWindow();
        stage.close();

    }

    @Override
    public void selectCells(List<Pair<Integer, Integer>> cells) {
        this.map.keySet().forEach(CardBox::disable);
        Platform.runLater(() -> this.map.keySet().stream()
                .filter(b -> cells.contains(this.map.get(b)))
                .forEach(c -> {
                    c.enable();
                    c.select();

                }));
    }

    @Override
    public void clearFieldSelections() {
        Platform.runLater(() -> this.map.keySet().forEach(c -> {
            c.unselect();
            c.enable();
        }));
    }

    @Override
    public void setCanPlay(Boolean b) {
        if (b) {
            this.turnCount++;
            this.turnCircle.setOpacity(1);
        } else {
            this.turnCircle.setOpacity(OPACITY);
        }
        this.canPlay = b;
        this.clearHandSelections();
    }

    @Override
    public void clearHandSelections() {
        Platform.runLater(() -> {
            this.handCards.forEach(CardBox::unselect);
            this.handCards.forEach(CardBox::enable);
        });
    }

    @Override
    public void showResultAndExit(Result result) {
        Platform.runLater(() -> {
            String res = "lose";
            if (result.equals(Result.WIN)) {
                res = "win";
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Game over");
            alert.setContentText("U " + res);
            alert.showAndWait();
            System.exit(0);
        });
    }

    @Override
    public void removeCardFromField(Pair<Integer, Integer> pos) {
        Platform.runLater(() -> this.map.keySet()
                .stream()
                .filter(a -> this.map.get(a).equals(pos))
                .forEach(CardBox::clearImage));
    }

}
