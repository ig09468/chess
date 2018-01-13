package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;
import java.util.ArrayList;

import static utils.ChessUtils.toCoord;

/**
 * Piece est la classe Mère correspondant aux pièces d'un plateau.
 * @author Kitei
 *
 */
public abstract class Piece {

    /* Couleur de la pièce */
    protected boolean white;

    /* Position actuelle de la pièce */
    protected Point position;

    /* Si elle est sur le plateau ou non */
    protected boolean onBoard;

    /* Ses mouvements légaux actuels */
    protected ArrayList <Point> legalMoves;


    /* Si elle n'a jamais été déplacée */
    protected boolean hasNeverMoved;

    /* Permet de dire que les mouvements actuels sont calculés */
    protected boolean legalMovesCalculated;
    /**
     * Constructor de la classe.
     * @param white, La couleur de la pièce
     * @param position, La position de la pièce
     *
     */
    public Piece(boolean white, Point position) {

        this.white = white;
        this.position=position;
        this.onBoard = true;
        this.hasNeverMoved = true;

    }


    /**
     * Fonction permettant de savoir si la piece est blanche
     *
     * @return true si la pièce est blanche
     */
    public boolean isWhite() {
        return white;
    }


    /**
     * Fonction permettant de retourner la position horizontale et verticale de la pièce.
     *
     * @return position
     */
    public Point getPosition() {
        return position;
    }


    /**
     * Fonction permettant de retourner si la pièce est sur le board
     * @return onBoard
     */
    public boolean getOnBoard(){
        return onBoard;
    }

    /**
     * Fonction pour savoir si la pièce a déjà bouger dans la partie
     * @return hasNeverMoved
     */
    public boolean getHasNeverMoved(){
        return hasNeverMoved;
    }



    public void legalMovesCalcutedForKnightAndKing(Board boardInstance,int initPositions[][]){
        Point testPos;
        Tile testTile;

        for (int direction = 0; direction < initPositions.length; direction++) {
            testPos = toCoord(new Point(this.position.x + initPositions[direction][0], this.position.y + initPositions[direction][1]));
            testTile = boardInstance.getTile(testPos);
            if (testTile!=null && !testTile.isOccupied()) { //la case est sur la table et inoccupée
                this.legalMoves.add(testPos);
            } else if (testTile!=null && testTile.getPiece().isWhite()!=this.white) {
                this.legalMoves.add(testPos);
            }
        }
    }
    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance Instance de plateau
     */
    public abstract void calculateLegalMoves(Board boardInstance);

    public void decimateLegalMovesCheck(Board boardInstance){
        if(this.legalMoves.size()>0){

        }
    }

    public String toString() {
        return white + " " + position.toString();
    }
}