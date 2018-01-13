package pieces;

import logique.Board;

import java.awt.*;

/**
 * Piece est la classe Mère correspondant aux pièces d'un plateau.
 * @author Kitei
 *
 */
public abstract class Piece {

    /* Couleur de la pièce */
    private boolean white;

    /* Position actuelle de la pièce */
    private Point position;

    /* Si elle est sur le plateau ou non */
    private boolean onBoard;

    /* Ses mouvements légaux actuels */
    private Point legalMove[];

    /* Si elle n'a jamais été déplacée */
    private boolean hasNeverMoved;


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
        if(this.legalMove.length>0){

        }
    }

    public String toString() {
        return white + " " + position.toString();
    }
}