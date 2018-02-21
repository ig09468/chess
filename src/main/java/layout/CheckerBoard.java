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
import java.net.URL;
import java.util.ArrayList;


public class CheckerBoard extends GridPane {
    private GridPane board;
    private TilePane[][] tiles = new TilePane[8][8];
    private ArrayList<Point> highlights;

    private static Image piecesImage=null;
    private static final int WHITE_OFFSET=0;
    private static final int BLACK_OFFSET=1;

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
                TilePane tile = tiles[column][7-row] = new TilePane(column, row);
                Rectangle rect = new Rectangle(60,60);
                rect.setStroke(Color.TRANSPARENT);
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStrokeWidth(0);
                rect.setFill(whiteTile ? Color.BEIGE : Color.DARKSLATEGRAY);
                tile.getChildren().add(rect);
                tile.setOnMouseClicked((e)->{tile.showLegalMoves();});
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
                URL imageURL = getClass().getClassLoader().getResource("chess_pieces.png");

                if (imageURL != null) {
                    piecesImage = new Image(imageURL.toExternalForm());
                }else
                {
                    System.err.println("Can't load resource chess_pieces.png");
                    return;
                }
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
                        TilePane tile = tiles[i][j];
                        ObservableList<Node> nodesInTile = tile.getChildren();
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
                                Rectangle2D rect = new Rectangle2D("KQBNRP".indexOf(piece.toShortName())*pieceWidth,(piece.isWhite() ? WHITE_OFFSET:BLACK_OFFSET)*pieceHeight ,pieceWidth, pieceHeight);
                                imgview.setViewport(rect);
                                imgview.setPreserveRatio(true);
                                imgview.setFitHeight(60);
                                nodesInTile.add(imgview);
                                tile.setBoard(boardInstance);
                            }
                        }

                    }

                }
            }
        }
    }

    public void setHighlight(ArrayList<Point> legalMoves) {
        if(highlights != null)
        {
            for(Point p: highlights)
            {
                TilePane tile = tiles[p.x][p.y];
                if(tile.getChildrenUnmodifiable().size() >=2)
                    tile.getChildren().remove(1);
            }
        }
        highlights = new ArrayList<>(legalMoves);
        Rectangle highlight = new Rectangle(60,60);
        highlight.setStroke(Color.TRANSPARENT);
        highlight.setStrokeType(StrokeType.INSIDE);
        highlight.setStrokeWidth(0);
        highlight.setFill(Color.LIGHTGREEN);
        for(Point p: highlights)
        {
            TilePane tile = tiles[p.x][p.y];
            tile.getChildren().add(1, highlight);
        }

    }

    public void setCannotMove(Point coord) {
        if(highlights != null)
        {
            for(Point p: highlights)
            {
                TilePane tile = tiles[p.x][p.y];
                if(tile.getChildrenUnmodifiable().size() >=2)
                    tile.getChildren().remove(1);
            }
        }
        highlights = new ArrayList<>();
        Rectangle highlight = new Rectangle(60,60);
        highlight.setStroke(Color.TRANSPARENT);
        highlight.setStrokeType(StrokeType.INSIDE);
        highlight.setStrokeWidth(0);
        highlight.setFill(Color.RED);
        highlights.add(coord);
        tiles[coord.x][coord.y].getChildren().add(1,highlight);
    }
}
