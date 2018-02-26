package layout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BaseLayout();
        primaryStage.setTitle("JavaChess");
        primaryStage.setScene(new Scene(root, 800, 600));
        Controller.mainStage = primaryStage;
        try
        {
            primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("icon.png")));
        }catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Cannot load icon : "+e.getMessage());
        }

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
