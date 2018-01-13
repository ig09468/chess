package pieces;

import logique.Board;

import java.awt.*;

public class Pawn extends Piece {

    public static final char SHORTNAME = 'P';

    /* Stock si le dernier mouvement est double. */
    private boolean LastMoveIsDouble;

    /* Coordonnées des prises enPassant. */
    private Point enPassantCapturePos[];

    /**
     * Constructor de la classe.
     * @param color La couleur de la piece
     * @param position Position de la piece
     */

    public Pawn(boolean color, Point position) {
        super(color, position);
        this.LastMoveIsDouble=false;
    }

    /**
     * Donne le si le dernier mouvement est double.
     * @return LastMoveIsDouble
     */
    public boolean getLastMoveIsDouble(){
        return LastMoveIsDouble;
    }


    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    /**
     * toShortString - Donne la représentation officielle de la pièce
     * @return Représentation de la pièce
     */
    public static String toShortString(){
        return "P";
    }

}
