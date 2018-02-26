package layout;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logique.GameResult;
import pieces.King;

public class EndResultPane extends BorderPane{
    public EndResultPane(Stage stage, GameResult result)
    {
        super();
        Button bOk = new Button("OK");
        bOk.setOnAction((e)-> stage.close());
        this.setBottom(bOk);
        BorderPane.setAlignment(bOk, Pos.CENTER);
        this.setStyle("-fx-border-color: black; -fx-border-width: 2px;-fx-border-style: solid inside");

        Label winlabel = new Label();
        if(result == GameResult.PAT)
        {
            winlabel.setText("Nul ½-½");
            this.setCenter(winlabel);
        }else
        {
            winlabel.setText(result == GameResult.WHITEWIN ? "Victoire des blancs ! 1-0" : "Victoire des noirs ! 0-1");
            ImageView image = new ImageView();
            image.setSmooth(true);
            image.setImage(Controller.loadPiecesImage());
            Rectangle2D rect = Controller.getViewRectangle(King.SHORTNAME, result == GameResult.WHITEWIN);
            image.setViewport(rect);
            image.setPreserveRatio(true);
            image.setFitHeight(120);
            this.setTop(winlabel);
            this.setCenter(image);

            BorderPane.setAlignment(image, Pos.CENTER);
        }
        BorderPane.setAlignment(winlabel, Pos.CENTER);
    }
}
