package views;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class GridFactory {

    public GridPane simpleSquareGrid(int size){
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(false);
        return gridPane;
    }

    public GridPane handGrid(int size) {
        return this.simpleSquareGrid(size);
    }
}
