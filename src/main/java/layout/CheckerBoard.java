package layout;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
                TilePane tile = tiles[column][7-row] = new TilePane(column, 7-row);
                Rectangle rect = new Rectangle(60,60);
                rect.setStroke(Color.TRANSPARENT);
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStrokeWidth(0);
                rect.setFill(whiteTile ? Color.BEIGE : Color.rgb(228,147,65));
                tile.getChildren().add(rect);
                tile.setOnMouseClicked((e)-> tile.clickHandle());
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
            piecesImage = Controller.loadPiecesImage();
            if(piecesImage !=null)
            {
                resetHighlight();
                for(int i=0; i<8;i++)
                {
                    for(int j=0;j<8;j++) {
                        Tile currTile = boardInstance.getTile(new Point(i, j));
                        if (currTile != null) {
                            TilePane tile = tiles[i][j];
                            tile.setBoard(boardInstance);
                            ObservableList<Node> nodesInTile = tile.getChildren();
                            if (nodesInTile != null) {
                                if (nodesInTile.size() >= 2) {
                                    nodesInTile.remove(1, nodesInTile.size());
                                }
                                if (currTile.isOccupied()) {
                                    Piece piece = currTile.getPiece();
                                    ImageView imgview = new ImageView();
                                    imgview.setSmooth(true);
                                    imgview.setImage(piecesImage);
                                    Rectangle2D rect = Controller.getViewRectangle(piece.toShortName(), piece.isWhite());
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

    public void setHighlight(Point selectedPiece, ArrayList<Point> legalMoves) {
        resetHighlight();
        highlights = new ArrayList<>(legalMoves);

        for(Point p: highlights)
        {
            Rectangle highlight = new Rectangle(60,60);
            TilePane tile = tiles[p.x][p.y];
            highlight.setStroke(tile.isOccupied() ? Color.ORANGE : Color.GREEN);
            highlight.setStrokeType(StrokeType.INSIDE);
            highlight.setStrokeWidth(2);
            highlight.setFill(tile.isOccupied() ? Color.YELLOW :Color.LIGHTGREEN);
            highlight.setOpacity(0.7);
            tile.getChildren().add(1, highlight);
        }

        Rectangle highlight = new Rectangle(60,60);
        TilePane tile = tiles[selectedPiece.x][selectedPiece.y];
        highlight.setStroke(Color.BLUE);
        highlight.setStrokeType(StrokeType.INSIDE);
        highlight.setStrokeWidth(2);
        highlight.setFill(Color.LIGHTBLUE);
        highlight.setOpacity(0.7);
        tile.getChildren().add(1, highlight);
        highlights.add(selectedPiece);

    }

    public void setCannotMove(Point coord) {
        resetHighlight();
        highlights = new ArrayList<>();
        Rectangle highlight = new Rectangle(60,60);
        highlight.setStroke(Color.DARKRED);
        highlight.setStrokeType(StrokeType.INSIDE);
        highlight.setStrokeWidth(2);
        highlight.setFill(Color.RED);
        highlight.setOpacity(0.7);
        highlights.add(coord);
        tiles[coord.x][coord.y].getChildren().add(1,highlight);
    }

    public void resetHighlight() {
        if(highlights != null)
        {
            for(Point p: highlights)
            {
                TilePane tile = tiles[p.x][p.y];
                if(tile.getChildrenUnmodifiable().size() >=2)
                    tile.getChildren().remove(1);
            }
            highlights.clear();
        }
    }
}
