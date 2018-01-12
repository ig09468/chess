package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les rois
 * @extends Piece
 *
 * */
public class King extends Piece {

    /**
     * Constructeur de la classe
     * @param color
     * @param position
     */
    public King(String color, Point position){
        super(color,position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    public static String toShortString(){
        return "K";
    }

}
