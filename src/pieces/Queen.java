package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les reines
 * @extends Piece
 *
 * */
public class Queen extends Piece {

    public static final char SHORTNAME = 'Q';

    public Queen(boolean color, Point position){
        super(color, position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    public static String toShortString() {
        return "Q";
    }

}
