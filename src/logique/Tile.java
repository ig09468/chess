package logique;

import pieces.Pieces;

import java.awt.*;

public class Tile {


    /* Position de la case */
    private Point position;

    /* Piece occupant la case */
    private Pieces piece;

    /*  */
    private boolean highlighted;

    public Tile(Point position, Pieces piece){

        this.position=position;
        this.piece=piece;

    }

    public boolean isOccupied(){
        return piece!=null;
    }

    public Pieces getPiece(){
        return piece;
    }

    public void setHighlight(boolean bHighlighted) {
        this.highlighted = bHighlighted;
    }



}
