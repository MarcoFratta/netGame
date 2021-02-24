package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.StackPane;

import javax.swing.*;


public class CardBox extends StackPane {

    public static final double PADDING = 0.15;
    private static final int BORDER_SIZE = 4;
    public static final double DISABLED_OPACITY = 0.4;
    private final ImageView imageView;
    private final String style;
    private boolean hide;

    public CardBox(double v) {
        this(v, 1, 1, 3);
        //super.getStyleClass().add("cardBox");
    }

    public CardBox(double v, int x, int y, int size) {
        super();
        System.out.println("v->" + v);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(v * PADDING));
        this.unselect();
        this.imageView = new ImageView();
        this.fixSize(v);


        //
        // this.imageView = new ImageView(getClass().getResource("/img_carte/napoletane/asso_bastoni.png").toString());
        this.getChildren().add(this.imageView);

        double defValue = (double) BORDER_SIZE / 2;
        double top = defValue,
                right = defValue,
                left = defValue,
                down = defValue;

        if (x == size - 1) {
            right = BORDER_SIZE;
        } else if (x == 0) {
            left = BORDER_SIZE;
        }
        if (y == size - 1) {
            down = BORDER_SIZE;
        } else if (y == 0) {
            top = BORDER_SIZE;
        }
        this.style = "-fx-border-width: " + top + " " + right + " " + down + " " + left + "; ";
        this.unselect();
        System.out.println("x ->" + x + " y ->" + y);
        System.out.println("-fx-border-width: " + top + " " + right + " " + down + " " + left + "; ");

    }

    public void fixSize(double v) {
        super.setMaxWidth(v);
        super.setMaxHeight(v);
        super.setMinHeight(v);
        super.setMinWidth(v);
        this.imageView.setFitHeight(v - (v * PADDING));
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(v - (v * PADDING));
        this.imageView.setStyle("-fx-alignment: CENTER ;");
        super.setMaxWidth(this.imageView.getFitWidth());
        super.setMaxHeight(this.imageView.getFitHeight());
    }

    public void select(){
        super.setStyle(this.style + "-fx-border-color: #e383fd;");
    }
    public void unselect(){
        super.setStyle(this.style + "-fx-border-color: #706f6f;");
    }

    public void setImage(Image i) {
        this.imageView.setImage(i);
    }

    public void clearImage() {
        this.imageView.setImage(null);
    }

    public void disable() {
        if (this.hide) {
            super.setStyle(this.style + "-fx-border-color: #202124;");
        }
        super.setOpacity(DISABLED_OPACITY);
    }

    public void enable() {
        super.setOpacity(1);
        this.unselect();
        ;
    }

    public boolean isFree() {
        return this.imageView.getImage() == null;
    }

    public void setHide() {
        this.hide = !this.hide;
    }
}
