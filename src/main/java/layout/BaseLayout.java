package layout;

import javafx.scene.layout.BorderPane;


public class BaseLayout extends BorderPane {
    public BaseLayout()
    {
        super();
        this.setTop(new Parametres());
        CheckerBoard checkerBoard = new CheckerBoard();
        Controller.checkerboard = checkerBoard;
        this.setCenter(checkerBoard);
    }
}
