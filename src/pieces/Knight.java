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
        if(boardInstance!=null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }

        this.legalMoves.clear();
        Point testPos;
        Tile testTile;
        int initPositions [][]= {{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}};
        for (int i = 0; i < initPositions.length; i++) {
            testPos = toCoord(new Point(this.position.x + initPositions[i][0], this.position.y + initPositions[i][1]));
            testTile = boardInstance.getTile(testPos);
            if (testTile!=null && !testTile.isOccupied()) { //la case est sur la table et inoccupée
                this.legalMoves.add(testPos);
            }
                                else if (testTile!=null && testTile.getPiece().isWhite()) {
                this.legalMoves.add(testPos);
            }
        }
        this.decimateLegalMovesCheck(boardInstance);
        this.legalMovesCalculated = true;
    }


    public static String toShortString(){
        return "N";
    }
}
