package layout;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logique.GameResult;

public class EndGameResultStage extends Stage {
    public EndGameResultStage(Stage mainStage, GameResult result)
    {
        super();
        this.setTitle(result == GameResult.PAT ? "Nul !" : "Echec et mat !");
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(mainStage);
        this.initStyle(StageStyle.UNDECORATED);
        this.setResizable(false);
        EndResultPane pane = new EndResultPane(this, result);
        Scene scene = new Scene(pane, 200,200);
        this.setScene(scene);
        this.showAndWait();
    }
}
