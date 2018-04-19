package layout;

import ia.AI;
import ia.AIThread;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class Parametres extends GridPane {
    //private static final String[] difficulties = {"0 - Aléatoire", "1 - Facile", "2 - Normal", "3 - Avancé"};
    public Parametres()
    {
        super();

        CheckBox whiteAICheckbox = new CheckBox("IA Blancs");
        CheckBox blackAICheckBox = new CheckBox("IA Noirs ");

        Slider whiteDifficultySlider = new Slider(0,8,3);
        Slider blackDifficultySlider = new Slider(0,8,3);

        whiteDifficultySlider.setDisable(true);
        whiteDifficultySlider.setMajorTickUnit(1);
        whiteDifficultySlider.setBlockIncrement(1);
        whiteDifficultySlider.setMinorTickCount(0);
        whiteDifficultySlider.setShowTickMarks(true);
        whiteDifficultySlider.setShowTickLabels(true);
        whiteDifficultySlider.setSnapToTicks(true);

        blackDifficultySlider.setDisable(true);
        blackDifficultySlider.setMajorTickUnit(1);
        blackDifficultySlider.setBlockIncrement(1);
        blackDifficultySlider.setMinorTickCount(0);
        blackDifficultySlider.setShowTickMarks(true);
        blackDifficultySlider.setShowTickLabels(true);
        blackDifficultySlider.setSnapToTicks(true);

        whiteAICheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> whiteDifficultySlider.setDisable(!newValue));
        blackAICheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> blackDifficultySlider.setDisable(!newValue));

        Label delayLabel = new Label("   Délai min :  ");
        TextField minDelayField = new TextField("2");
        Button newGameButton = new Button("Nouvelle Partie");
        Label meanLabel = new Label("");
        Controller.meanComputingLabel = meanLabel;

        Button undo = new Button("Undo");
        Controller.undobutton = undo;

        addRow(0, whiteAICheckbox, new Label(" "), whiteDifficultySlider, new Label("      "), newGameButton, delayLabel, minDelayField);
        addRow(1, blackAICheckBox, new Label(),blackDifficultySlider, new Label(), undo, meanLabel);

        setAlignment(Pos.TOP_CENTER);
        delayLabel.setContentDisplay(ContentDisplay.RIGHT);
        minDelayField.setMaxWidth(30);

        newGameButton.setOnAction((e)-> {
            if(Controller.whiteThread != null)
            {
                if(Controller.whiteThread.isAlive())
                {
                    try {
                        Controller.whiteThread.terminate();
                        Controller.whiteThread.join();
                    } catch (InterruptedException e1) {
                        e1.getMessage();
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
                    }
                }
                Controller.blackThread=null;
            }
            Controller.computingLabel.setText("");
            Controller.newGame(whiteDifficultySlider.getValue(), blackDifficultySlider.getValue(), whiteAICheckbox.isSelected(), blackAICheckBox.isSelected(), minDelayField.getText());
            AIThread.times.clear();
            Controller.meanComputingLabel.setText("");
        });
        undo.setOnAction((e)->{
            Controller.currentGame.getBoard().fullUndo();
            Controller.checkerboard.updateBoard(Controller.currentGame.getBoard());
            if(Controller.currentGame.getBoard().getLastMove() == null)
                undo.setDisable(true);
        });
        undo.setDisable(true);
    }
}
