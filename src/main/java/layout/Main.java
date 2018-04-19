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
        BorderPane root = new BaseLayout();
        primaryStage.setTitle("JavaChess");
        primaryStage.setScene(new Scene(root, 800, 650));
        Controller.mainStage = primaryStage;
        try
        {
            primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("icon.png")));
        }catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Cannot load icon : "+e.getMessage());
        }
        primaryStage.setOnCloseRequest((e)->{
            if(Controller.whiteThread != null)
            {
                if(Controller.whiteThread.isAlive())
                {
                    try {
                        Controller.whiteThread.terminate();
                        Controller.whiteThread.join();
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                        Controller.boardLock=false;
                    }
                }
                Controller.whiteThread=null;
            }
            if(Controller.blackThread != null)
            {
                if(Controller.blackThread.isAlive())
                {
                    try{
                        Controller.blackThread.terminate();
                        Controller.blackThread.join();
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                        Controller.boardLock=false;
                    }
                }
                Controller.blackThread=null;
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
