package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les reines
 * @extends Piece
 *
 * */
public class Queen extends Piece {

    public Queen(String color, Point position){
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
