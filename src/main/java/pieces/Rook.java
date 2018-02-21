package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;

import static utils.ChessUtils.toCoord;

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
        if(boardInstance==null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }

        this.legalMoves.clear();
        Point testPos;
        Tile testTile;
        int initPositions[][] = {{0, -1}, {0, 1}, {1, 0}, {-1, 0} };
        int modifierX=0, modifierY=0;

        for (int direction = 0; direction < 4; direction++) {
            testPos = toCoord(new Point(this.position.x + initPositions[direction][0], this.position.y + initPositions[direction][1]));
            for (int distance = 0; testPos != null && distance<7; distance++) {
                testTile = boardInstance.getTile(testPos);
                if (!testTile.isOccupied()) {
                    this.legalMoves.add(testPos);
                    switch(direction) {
                        case 0: modifierY=-1; break;
                        case 1: modifierY=1; break;
                        case 2: modifierX=1; break;
                        case 3: modifierX=-1; break;
                    }
                    testPos = toCoord(new Point(testPos.x + modifierX, testPos.y + modifierY));
                }
                else {
                    //si la pièce occupante est de couleur inverse, ajout de la position en legalMoves
                    if (testTile.getPiece().isDiffColor(this.white)){
                        this.legalMoves.add(testPos);
                    }
                    //sinon, ne rien faire
                    //sortir de la boucle
                    break;
                }
            }
        }
        this.legalMovesCalculated = true;
        this.decimateLegalMovesCheck(boardInstance);

    }

    public static String toShortString(){
        return "R";
    }
    public char toShortName(){ return SHORTNAME; }
}
