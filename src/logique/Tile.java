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

    public boolean isOccupied(){
        return piece!=null;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setHighlight(boolean bHighlighted) {
        this.highlighted = bHighlighted;
    }



}
