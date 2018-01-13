package pieces;

import logique.Board;

import java.awt.*;
import java.util.ArrayList;

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