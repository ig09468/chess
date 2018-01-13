package layout;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class CheckerBoard extends GridPane {
    private GridPane board;
    private Rectangle[][] tiles = new Rectangle[8][8];
    public CheckerBoard()
    {

        boolean whiteTile=true;
        for(int i=1; i<9;i++)
        {
            Label leftLetter = new Label((char)((int)'A' +i-1) + " ");
            leftLetter.setMinHeight(60);
            add(leftLetter, 0, i);

        }
        board = new GridPane();
        board.setStyle("-fx-border-color: black; -fx-border-width: 1px;-fx-border-style: solid inside");
        for(int column=0; column<8; column++)
        {
            Label topLabel = new Label(String.valueOf(column+1));
            topLabel.setMinWidth(60);
            add(topLabel, column+1, 0);
            topLabel.setAlignment(Pos.CENTER);
            for(int row=0; row<8; row++)
            {
                Rectangle rect = tiles[column][7-row] = new Rectangle(60,60);
                rect.setStroke(Color.TRANSPARENT);
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStrokeWidth(0);
                rect.setFill(whiteTile ? Color.BEIGE : Color.DARKSLATEGRAY);
                whiteTile = !whiteTile;
                board.add(rect, column, row);
            }
            whiteTile = !whiteTile;
            Label bottomLabel = new Label(String.valueOf(column+1));
            bottomLabel.setMinWidth(60);
            add(bottomLabel, column+1, 9);
            bottomLabel.setAlignment(Pos.CENTER);
        }
        add(board, 1, 1, 8,8);
        for(int i=1; i<9;i++)
        {
            Label rightLetter = new Label(" " + (char)((int)'A' +i-1));
            rightLetter.setMinHeight(60);
            add(rightLetter, 9, i);
        }
        setAlignment(Pos.CENTER);
    }


}
