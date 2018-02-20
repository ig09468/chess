package layout;

import javafx.scene.layout.BorderPane;

public class BaseLayout extends BorderPane {
    public BaseLayout()
    {
        super();
        this.setTop(new Parametres());
        this.setCenter(new CheckerBoard());
    }
}
