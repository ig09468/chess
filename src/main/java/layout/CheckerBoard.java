package layout;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import logique.Board;
import logique.Tile;
import pieces.Piece;

import java.awt.*;


public class CheckerBoard extends GridPane {
    private GridPane board;
    private StackPane[][] tiles = new StackPane[8][8];
    private ImageView[][] tilesImage = new ImageView[8][8];

    private static Image piecesImage=null;
    private static int KING_OFFSET=0;
    private static int QUEEN_OFFSET=1;
    private static int BISHOP_OFFSET=2;
    private static int KNIGHT_OFFSET=3;
    private static int ROOK_OFFSET=4;
    private static int PAWN_OFFSET=5;
    private static int WHITE_OFFSET=0;
    private static int BLACK_OFFSET=1;

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
                StackPane tile = tiles[column][7-row] = new StackPane();
                Rectangle rect = new Rectangle(60,60);
                rect.setStroke(Color.TRANSPARENT);
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStrokeWidth(0);
                rect.setFill(whiteTile ? Color.BEIGE : Color.DARKSLATEGRAY);
                tile.getChildren().add(rect);
                whiteTile = !whiteTile;
                board.add(tile, column, row);
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


    public void updateBoard(Board boardInstance) {
        if(boardInstance != null)
        {
            if(piecesImage ==null)
            {
                piecesImage = new Image(Image.class.getResource("chess_pieces.png").getPath());
            }
            int pieceHeight = (int)(piecesImage.getHeight()/2);
            int pieceWidth = (int)(piecesImage.getWidth()/6);
            for(int i=0; i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    Tile currTile =  boardInstance.getTile(new Point(i,j));
                    if(currTile != null)
                    {
                        ObservableList<Node> nodesInTile = tiles[i][j].getChildren();
                        if(nodesInTile!= null)
                        {
                            if(nodesInTile.size() >=2)
                            {
                                nodesInTile.remove(1,nodesInTile.size());
                            }
                            if(currTile.isOccupied())
                            {
                                Piece piece = currTile.getPiece();
                                ImageView imgview = new ImageView();
                                imgview.setImage(piecesImage);
                                Rectangle2D rect = new Rectangle2D("KQBNRP".indexOf(piece.toShortName()),(piece.isWhite() ? WHITE_OFFSET:BLACK_OFFSET)*pieceHeight ,pieceWidth, pieceHeight);
                                imgview.setViewport(rect);
                                imgview.setPreserveRatio(true);
                                imgview.setFitHeight(60);
                                nodesInTile.add(imgview);
                            }
                        }

                    }

                }
            }
        }
    }
}
