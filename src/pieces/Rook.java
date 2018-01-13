package pieces;

import logique.Board;

import java.awt.*;

public class Rook extends Piece {

    public static final char SHORTNAME = 'R';
    /**
     * Constructeur de la classe
     * @param color La couleur de la piece
     * @param position La position de la piece
     */
    public Rook(boolean color, Point position) {
        super(color, position);
    }


    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }

    public static String toShortString(){
        return "R";
    }
}
