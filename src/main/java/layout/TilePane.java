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

    public void clickHandle()
    {
        if (!Controller.boardLock && board != null && !(((Controller.currentGame.getWhiteAILevel() != -1) && board.isWhiteTurn()) || ((Controller.currentGame.getBlackAILevel() != -1) && !board.isWhiteTurn()))) {
            Piece piece = board.getPiece(coord);
            //On a cliqué sur une piece qui doit jouer, on affiche les mouvements légaux
            if (piece != null && piece.isWhite() == board.isWhiteTurn()) {
                if (!piece.isLegalMovesCalculated())
                    piece.calculateLegalMoves(board);
                if (piece.canMove()) {
                    Controller.checkerboard.setHighlight(coord, piece.getLegalMoves());
                } else {
                    Controller.checkerboard.setCannotMove(coord);
                }
                board.setSelectedPiece(piece);
            } else //On a cliqué sur une case vide ou une piece adverse, on vérifie si on peut effectuer un mouvement
            {

                Piece pieceSelected = board.getSelectedPiece();
                if (pieceSelected != null && pieceSelected.isLegalMove(coord)) {
                    Controller.checkerboard.resetHighlight();
                    board.move(pieceSelected.getPosition(), coord);
                    Controller.doNextMove();
                }
            }
        }
    }

    public boolean isOccupied() {
        return board != null && board.getPiece(coord) != null;
    }
}
