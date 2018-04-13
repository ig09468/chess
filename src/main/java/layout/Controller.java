package layout;

import ia.AIMovement;
import ia.ZobristHash;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logique.Board;
import logique.Game;
import logique.GameResult;

import java.net.URL;

public class Controller {
    public static Stage mainStage;
    public static Game currentGame;
    public static CheckerBoard checkerboard;
    public static Label computingLabel;

    public static Image piecesImage = null;
    private static final String piecesImageOrder = "KQBNRP";
    private static final int WHITE_OFFSET=0;
    private static final int BLACK_OFFSET=1;
    private static int pieceWidth=0;
    private static int pieceHeight=0;
    public static boolean autoplayActive=false;
    public static Thread autoplayThread;
    public static final ZobristHash zobrist = new ZobristHash();

    public static Thread whiteThread = null;
    public static Thread blackThread = null;
    public static boolean boardLock = false;



    public static void newGame(String whiteAIComboText, String blackAIComboText, boolean whiteAIActivated, boolean blackAIActivated,String delayText)
    {
        try
        {
            currentGame = new Game(whiteAIActivated ? Parametres.stringToDifficultyLevel(whiteAIComboText) : -1, blackAIActivated ? Parametres.stringToDifficultyLevel(blackAIComboText) : -1, Integer.valueOf(delayText));
            showGame();
            if(currentGame.getWhiteAILevel() > -1)
            {
                doNextMove();
            }
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
            EndGameResultStage endGameResultStage = new EndGameResultStage(mainStage, GameResult.WHITEWIN);
        }else
        {
            EndGameResultStage endGameResultStage = new EndGameResultStage(mainStage, GameResult.BLACKWIN);
        }
    }

    public static void staleMate()
    {
        EndGameResultStage endGameResultStage = new EndGameResultStage(mainStage, GameResult.PAT);
    }

    public static Image loadPiecesImage()
    {
        if(piecesImage==null)
        {
            URL imageURL = checkerboard.getClass().getClassLoader().getResource("chess_pieces.png");

            if (imageURL != null) {
                piecesImage = new Image(imageURL.toExternalForm());
            }else
            {
                System.err.println("Can't load resource chess_pieces.png");
            }

        }
        if(piecesImage != null)
        {
            pieceHeight = (int)(piecesImage.getHeight()/2);
            pieceWidth = (int)(piecesImage.getWidth()/6);
        }
        return piecesImage;
    }

    public static Rectangle2D getViewRectangle(char pieceShortName, boolean isWhite)
    {
        return new Rectangle2D(piecesImageOrder.indexOf(pieceShortName) * pieceWidth, (isWhite ? WHITE_OFFSET : BLACK_OFFSET) * pieceHeight, pieceWidth, pieceHeight);
    }

    public static void doNextMove()
    {
        Board board = currentGame.getBoard();
        if(board.isWhiteTurn())
        {
            if(currentGame.getWhiteAILevel() > -1)
            {
                boardLock = true;
                long start = System.currentTimeMillis();
                Controller.computingLabel.setText("White AI computing next move...");
                whiteThread = new Thread(()->
                {
                   AIMovement move = Controller.currentGame.getWhiteAI().getNextMove();
                   if(move != null)
                   {
                       Platform.runLater(()->{
                           currentGame.getBoard().move(move.getFrom(), move.getTo());
                           long end = System.currentTimeMillis();
                           Controller.computingLabel.setText("White move computed in " + (end-start)/1000 + "."+String.format("%3d",(end-start)%1000)+"s");
                           boardLock=false;
                           doNextMove();
                       });
                   }
                });
                whiteThread.start();
            }
        }else
        {
            if(currentGame.getBlackAILevel() > -1)
            {
                boardLock = true;
                long start = System.currentTimeMillis();
                Controller.computingLabel.setText("Black AI computing next move...");
                blackThread = new Thread(()->
                {
                    AIMovement move = Controller.currentGame.getBlackAI().getNextMove();
                    if(move != null)
                    {
                        Platform.runLater(()->{
                            currentGame.getBoard().move(move.getFrom(), move.getTo());
                            long end = System.currentTimeMillis();
                            Controller.computingLabel.setText("Black move computed in " + (end-start)/1000 + "."+String.format("%3d",(end-start)%1000)+"s");
                            boardLock=false;
                            doNextMove();
                        });
                    }
                });
                blackThread.start();
            }
        }
    }
}
