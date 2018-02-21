package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;

import static utils.ChessUtils.toCoord;

/** Classe pour les cavaliers
 * @extends Piece
 *
 * */
public class Knight extends Piece {

    public static final char SHORTNAME = 'N';

    public Knight(boolean color, Point position){
        super(color, position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){
        if(boardInstance==null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }

        this.legalMoves.clear();
        Point testPos;
        Tile testTile;
        int initPositions [][]= {{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}};
        legalMovesCalculatedForKnightAndKing(boardInstance,initPositions);
        this.legalMovesCalculated = true;
        this.decimateLegalMovesCheck(boardInstance);

    }


    public static String toShortString(){
        return "N";
    }
    public char toShortName(){ return SHORTNAME; }
}
