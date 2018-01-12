package pieces;

import logique.Board;

import java.awt.*;

public class Pawn extends Pieces {

    /* Stock si le dernier mouvement est double. */
    private boolean LastMoveIsDouble;

    /* Coordonnées des prises enPassant. */
    private Point enPassantCapturePos[];

    /**
     * Constructor de la classe.
     * @param color
     * @param posX
     * @param posY
     */
    public Pawn(String color, int posX, int posY) {
        super(color, posX, posY);
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
     *
     * @return
     */
    public static String toShortString(){
        return "P";
    }

    /**
     *
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

    }
}
