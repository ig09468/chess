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
        calculateLegalMoves(boardInstance) {
            if(!boardInstance || !(boardInstance.constructor && boardInstance.constructor.name === "Board"))

            {

                console.error("Board undefined for calculateLegalMoves()");

                return;
            }
            this.legalMoves.length = 0;
            var testPos, testTile;
            var initPositions = [[0, -1], [0, 1], [1, 0], [-1, 0]];
            for (var j = 0; j < 4; j++) {
                testPos = utilities.toCoord({"x": this.pos.x + initPositions[j][0], "y": this.pos.y + initPositions[j][1]});
                for (var i = 0; testPos != undefined && i<7; i++) {
                    testTile = boardInstance.getTile(testPos);
                    if (!testTile.isOccupied()) {
                        this.legalMoves.push(testPos);
                        var modifierX=0, modifierY=0;
                        switch(j) {
                            case 0: modifierY=-1; break;
                            case 1: modifierY=1; break;
                            case 2: modifierX=1; break;
                            case 3: modifierX=-1; break;
                        }
                        testPos = utilities.toCoord({"x": testPos.x + modifierX, "y": testPos.y + modifierY});
                    }
                                        else {
                        //si la pièce occupante est de couleur inverse, ajout de la position en legalMoves
                        if (testTile.getPiece().getColor() != this.color) {
                            this.legalMoves.push(testPos);
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
    }

    public static String toShortString(){
        return "R";
    }
}
