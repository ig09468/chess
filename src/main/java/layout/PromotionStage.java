package layout;

import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PromotionStage extends Stage{
    public char promotion = ' ';
    public PromotionStage(boolean isWhite)
    {
        this.setTitle("Choix de la promotion");
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Controller.mainStage);
        this.initStyle(StageStyle.UNDECORATED);
        this.setResizable(false);
        PromotionPane pane = new PromotionPane(this, isWhite);
        Scene scene = new Scene(pane, 800, 200);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(25);
        pane.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        column.setPercentWidth(25);
        pane.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        column.setPercentWidth(25);
        pane.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        column.setPercentWidth(25);
        pane.getColumnConstraints().add(column);
        this.setScene(scene);
        this.showAndWait();
    }

}
