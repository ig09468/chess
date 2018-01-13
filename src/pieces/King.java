package pieces;

import logique.Board;

import java.awt.*;

/** Classe pour les rois
 * @extends Piece
 *
 * */
public class King extends Piece {

    public static final char SHORTNAME = 'K';
    /**
     * Constructeur de la classe
     * @param color
     * @param position
     */
    public King(boolean color, Point position){
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
