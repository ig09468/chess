package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les fous
 * @extends Piece
 *
 * */

public class Bishop extends Piece {

    public static final char SHORTNAME = 'B';

    public Bishop(boolean color, Point position){
        super(color, position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    public static String toShortString(){
        return "B";
    }
}
