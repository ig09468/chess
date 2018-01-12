package logique;

import pieces.Piece;

import java.awt.*;

public class Tile {


    /* Position de la case */
    private Point position;

    /* Piece occupant la case */
    private Piece piece;

    /*  */
    private boolean highlighted;

    public Tile(Point position, Piece piece){

        this.position=position;
        this.piece=piece;

    }

    /**
     * Permet de vérifier si la case est occupée
     * @return
     */
    public boolean isOccupied(){
        return piece!=null;
    }

    /**
     * Retourne la piece occupant la case
     * @return
     */
    public Piece getPiece(){
        return piece;
    }



}
