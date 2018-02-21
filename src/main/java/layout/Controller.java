package layout;

import javafx.scene.image.Image;
import logique.Board;
import logique.Game;

public class Controller {
    public static Game currentGame;
    public static CheckerBoard checkerboard;

    public static void newGame(String whiteAIComboText, String blackAIComboText, String delayText)
    {
        try
        {
            currentGame = new Game(Parametres.stringToDifficultyLevel(whiteAIComboText), Parametres.stringToDifficultyLevel(blackAIComboText), Integer.valueOf(delayText));
            showGame();
        }catch(NumberFormatException e)
        {
            e.printStackTrace();
            System.err.println("Number format error in delay");
        }
    }

    public static void showGame()
    {
        Board board = currentGame.getBoard();
        checkerboard.updateBoard(board);

    }

    public static void checkMate(boolean whiteWin)
    {
        if(whiteWin)
        {
            //TODO Affiche la victoire des blancs
        }else
        {
            //TODO Affiche la victoire des noirs
        }
    }

    public static void staleMate()
    {
        //TODO Affiche le pat (pas de vainqueur)
    }

    private static void loadPiecesImage()
    {

    }
}
