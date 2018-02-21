package layout;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import logique.Board;
import pieces.Piece;

import java.awt.*;

public class TilePane extends StackPane {
    private Point coord;
    private Board board;

    public TilePane(int column, int row)
    {
        super();
        coord = new Point(column, row);
    }

    public TilePane(int column, int row, Node... children)
    {
        super(children);
        coord = new Point(column, row);
    }


    public void setBoard(Board board) {
        this.board = board;
    }

    public void showLegalMoves()
    {
        if(board != null)
        {
            Piece piece = board.getPiece(coord);
            if(piece != null)
            {
                if(piece.isLegalMovesCalculated())
                    piece.calculateLegalMoves(board);
                if(piece.canMove())
                {
                    Controller.checkerboard.setHighlight(piece.getLegalMoves());
                }else
                {
                    Controller.checkerboard.setCannotMove(coord);
                }
            }
        }
    }
}
