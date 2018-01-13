package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;

import static utils.ChessUtils.toCoord;

/** Classe pour les fous
 * @extends Piece
 *
 * */

public class Bishop extends Piece {

    public static final char SHORTNAME = 'B';

    public Bishop(boolean color, Point position){
        super(color, position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance) {


        /* Vérification de boardInstance */
        if(boardInstance!=null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }
        /*                              */
        this.legalMoves.clear();

        /*                              */
        Point testPos;
        Tile testTile;
        int modifierX=0, modifierY=0;
        int initPositions[][] = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1} };


        /*                              */
        for (int j = 0; j < 4; j++) {
            testPos = toCoord(new Point(this.position.x + initPositions[j][0],  this.position.y + initPositions[j][1]);

            /*                          */
            for (int i = 0; testPos != null && i<7; i++) {
                testTile = boardInstance.getTile(testPos);

                if (testTile!=null && !testTile.isOccupied()) {
                    this.legalMoves.add(testPos);
                    /*                      */
                    switch(j) {
                        case 0: modifierX=1; modifierY=1; break;
                        case 1: modifierX=1; modifierY=-1; break;
                        case 2: modifierX=-1; modifierY=1; break;
                        case 3: modifierX=-1; modifierY=-1; break;
                    }
                    testPos = toCoord(new Point(testPos.x + modifierX, testPos.y + modifierY));
                }else
                    {
                    //si la pièce occupante est de couleur inverse, ajout de la position en legalMoves
                    if (testTile!=null && testTile.getPiece().isWhite()) {
                        this.legalMoves.add(testPos);
                    }
                        //sinon, ne rien faire
                        //sortir de la boucle
                        break;
                    }
            }
        }
        this.decimateLegalMovesCheck(boardInstance);
        this.legalMovesCalculated = true;
    }


    public static String toShortString(){
        return "B";
    }
}
