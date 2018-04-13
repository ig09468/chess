package layout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        /*try {
            System.setOut(new PrintStream(new FileOutputStream("out.txt")));
            //System.setErr(new PrintStream(new FileOutputStream("err.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
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
