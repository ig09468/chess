package layout;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class BaseLayout extends BorderPane {
    public BaseLayout()
    {
        super();
        this.setTop(new Parametres());
        CheckerBoard checkerBoard = new CheckerBoard();
        Controller.checkerboard = checkerBoard;
        this.setCenter(checkerBoard);
        Controller.computingLabel = new Label();
        this.setBottom(Controller.computingLabel);
    }
}
