package pieces;

import logique.Board;

import java.awt.*;

/**
 * Piece est la classe Mère correspondant aux pièces d'un plateau.
 * @author Kitei
 *
 */
public class Piece {

    /* Couleur de la pièce */
    private String color;

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
     * @param color, est la couleur de la pièce
     * @param posX,  la position horizontale
     * @param posY,  la position verticale
     *
     */
    public Piece(String color, int posX, int posY) {

        this.color = color;
        this.position.x = posX;
        this.position.y = posY;
        this.onBoard = true;
        this.hasNeverMoved = true;

    }


    /**
     * Fonction permettant de retourner la couleur de la pièce.
     *
     * @return color, la couleur.
     */
    public String getColor() {
        return color;
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
     * @return onBoard;
     */
    public boolean getOnBoard(){
        return onBoard;
    }

    /**
     * Fonction pour savoir si la pièce a déjà bouger dans la partie
     * @return 
     */
    public boolean getHasNeverMoved(){
        return hasNeverMoved;
    }


    public void decimateLegalMovesCheck(Board boardInstance){
        if(this.legalMove.length>0){

        }
    }

    public String toString() {
        return color + " " + position.toString();
    }
}