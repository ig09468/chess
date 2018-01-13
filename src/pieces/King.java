package pieces;

import logique.Board;
import logique.Tile;

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
        if (boardInstance != null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }

        this.legalMoves.clear();
        Point testPos;
        Tile testTile;
        int modifierX = 0, modifierY = 0;
        int initPositions[][] = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};


    }

    public static String toShortString(){
        return "K";
    }

}
