package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les cavaliers
 * @extends Piece
 *
 * */
public class Knight extends Piece {

    public Knight(String color, Point position){
        super(color, position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    public static String toShortString(){
        return "N";
    }
}
