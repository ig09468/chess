package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;

import static utils.ChessUtils.toCoord;

/** Classe pour les rois
 * @extends Piece
 *
 * */
public class King extends Piece {

    private boolean littleCastle;

    private boolean bigCastle;

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

        int modifierX = 0, modifierY = 0;
        int initPositions[][] = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        legalMovesCalcutedForKnightAndKing(boardInstance,initPositions);

        this.decimateLegalMovesCheck(boardInstance);
        if((testPos = this.checkBigCastle(boardInstance)))
        {
            this.legalMoves.add(testPos);
            this.bigCastle = true;
        }
        if((testPos = this.checkLittleCastle(boardInstance)))
        {
            this.legalMoves.add(testPos);
            this.littleCastle = true;
        }
        this.legalMovesCalculated = true;
    }


    public boolean getLittleCastle(){
        return this.littleCastle;
    }

    public boolean getBigCastle(){
        return this.bigCastle;
    }

    public static String toShortString(){
        return "K";
    }

}
