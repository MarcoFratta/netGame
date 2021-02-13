package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class CardBox extends StackPane {

    public static final int padding = 10;
    private final ImageView imageView;

    public CardBox(double v){
        System.out.println("v->"+v);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(padding,padding,padding,padding));
        this.unselect();

        this.imageView = new ImageView();
        //
        // this.imageView = new ImageView(getClass().getResource("/img_carte/napoletane/asso_bastoni.png").toString());
        this.getChildren().add(this.imageView);
        this.imageView.setFitHeight(v-(v/2));
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(v-(v/2));
        //this.imageView.setStyle("-fx-alignment: CENTER");
        super.setMaxWidth(this.imageView.getFitWidth());
        super.setMaxHeight(this.imageView.getFitHeight());


    }
    public void select(){
        super.setStyle("-fx-border-color: #50ff50");
    }
    public void unselect(){
        super.setStyle("-fx-border-color: #000000");
    }

    public void setImage(Image i){
        this.imageView.setImage(i);
    }

    public void clearImage(){
        this.imageView.setImage(null);
    }

    public boolean isFree(){
        return this.imageView.getImage() == null;
    }

}
