package layout;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pieces.Bishop;
import pieces.Knight;
import pieces.Queen;
import pieces.Rook;


public class PromotionPane extends GridPane {

    private static double s=1;
    private static double u=0.5;

    public PromotionPane(PromotionStage stage, boolean isWhite)
    {
        Label text = new Label("Choisissez la promotion du pion :");
        this.add(text, 0, 0, 4, 1);
        this.setStyle("-fx-border-color: black; -fx-border-width: 2px;-fx-border-style: solid inside");

        ImageView imageQ = new ImageView();
        ImageView imageN = new ImageView();
        ImageView imageR = new ImageView();
        ImageView imageB = new ImageView();

        imageQ.setSmooth(true);
        imageN.setSmooth(true);
        imageR.setSmooth(true);
        imageB.setSmooth(true);

        imageQ.setImage(Controller.loadPiecesImage());
        imageN.setImage(Controller.loadPiecesImage());
        imageR.setImage(Controller.loadPiecesImage());
        imageB.setImage(Controller.loadPiecesImage());

        imageQ.setViewport(Controller.getViewRectangle(Queen.SHORTNAME, isWhite));
        imageN.setViewport(Controller.getViewRectangle(Knight.SHORTNAME, isWhite));
        imageR.setViewport(Controller.getViewRectangle(Rook.SHORTNAME, isWhite));
        imageB.setViewport(Controller.getViewRectangle(Bishop.SHORTNAME, isWhite));

        imageQ.setPreserveRatio(true);
        imageN.setPreserveRatio(true);
        imageR.setPreserveRatio(true);
        imageB.setPreserveRatio(true);

        imageQ.setFitHeight(120);
        imageN.setFitHeight(120);
        imageR.setFitHeight(120);
        imageB.setFitHeight(120);

        imageQ.setOpacity(u);
        imageN.setOpacity(u);
        imageR.setOpacity(u);
        imageB.setOpacity(u);

        Button validate = new Button("OK");
        validate.setDisable(true);
        validate.setOnAction((e)-> stage.close());

        imageQ.setOnMouseClicked((e)->{imageQ.setOpacity(s);imageN.setOpacity(u);imageR.setOpacity(u);imageB.setOpacity(u);stage.promotion='Q';validate.setDisable(false);});
        imageN.setOnMouseClicked((e)->{imageQ.setOpacity(u);imageN.setOpacity(s);imageR.setOpacity(u);imageB.setOpacity(u);stage.promotion='N';validate.setDisable(false);});
        imageR.setOnMouseClicked((e)->{imageQ.setOpacity(u);imageN.setOpacity(u);imageR.setOpacity(s);imageB.setOpacity(u);stage.promotion='R';validate.setDisable(false);});
        imageB.setOnMouseClicked((e)->{imageQ.setOpacity(u);imageN.setOpacity(u);imageR.setOpacity(u);imageB.setOpacity(s);stage.promotion='B';validate.setDisable(false);});

        this.addRow(1,imageQ, imageN, imageR, imageB);
        this.add(validate,0,2,4,1);

        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setHalignment(validate, HPos.CENTER);
        GridPane.setHalignment(imageN, HPos.CENTER);
        GridPane.setHalignment(imageR, HPos.CENTER);
        GridPane.setHalignment(imageB, HPos.CENTER);
    }
}
