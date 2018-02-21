package layout;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class Parametres extends GridPane {
    private static final String[] difficulties = {"0 - Aléatoire", "1 - Facile", "2 - Normal", "3 - Avancé"};
    public Parametres()
    {
        super();
        ObservableList<String> difficultiesList = FXCollections.observableArrayList();
        difficultiesList.addAll(difficulties);

        CheckBox whiteAICheckbox = new CheckBox("IA Blancs");
        CheckBox blackAICheckBox = new CheckBox("IA Noirs ");

        ComboBox<String> whiteDifficultyComboBox = new ComboBox<>();
        ComboBox<String> blackDifficultyComboBox = new ComboBox<>();
        whiteDifficultyComboBox.setItems(difficultiesList);
        blackDifficultyComboBox.setItems(difficultiesList);
        whiteDifficultyComboBox.setDisable(true);
        blackDifficultyComboBox.setDisable(true);
        whiteDifficultyComboBox.getSelectionModel().select(2);
        blackDifficultyComboBox.getSelectionModel().select(2);

        whiteAICheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> whiteDifficultyComboBox.setDisable(!newValue));
        blackAICheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> blackDifficultyComboBox.setDisable(!newValue));

        Label delayLabel = new Label("   Délai min :  ");
        TextField minDelayField = new TextField("2");
        Button newGameButton = new Button("Nouvelle Partie");

        addRow(0, whiteAICheckbox, new Label(" "), whiteDifficultyComboBox, new Label("      "), newGameButton, delayLabel, minDelayField);
        addRow(1, blackAICheckBox, new Label(),blackDifficultyComboBox, new Label());

        setAlignment(Pos.TOP_CENTER);
        delayLabel.setContentDisplay(ContentDisplay.RIGHT);
        minDelayField.setMaxWidth(30);

        newGameButton.setOnAction((e)->{Controller.newGame(whiteDifficultyComboBox.getValue(), blackDifficultyComboBox.getValue(), minDelayField.getText());});
    }

    public static int stringToDifficultyLevel(String diffString)
    {
        for(int i=0; i<difficulties.length; i++)
        {
            if(diffString.equals(difficulties[i]))
                return i;
        }
        return -1;
    }
}
