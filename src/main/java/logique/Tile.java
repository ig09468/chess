package logique;

import pieces.*;

import java.awt.*;
import java.util.ArrayList;

import static utils.ChessUtils.toCoord;

public class Tile {


    /* Position de la case */
    private Point position;

    /* Piece occupant la case */
    private Piece piece;


    /**
     * Constructeur de la classe.
     * @param position La position de la pièce
     * @param piece La pièce dans cette case
     */
    public Tile(Point position, Piece piece){

        this.position=position;
        this.piece=piece;

    }

    public Point getPosition() {
        return position;
    }

    /**
     * isInCheckTestWithPawn - Vérifie si le roi est en échec en vérifiant s'il y a un Pawn.
     * @param boardInstance La plateau actuel
     * @return Retourne vrai s'il y a un Pawn sinon faux
     */
    public boolean isAttackedByPawn(Board boardInstance, boolean isWhite){
        Point testPos;
        Tile testTile;

        int posY = this.position.y + ( isWhite ? 1 : -1);
        testPos = new Point(position.x+1, posY);
        testTile = boardInstance.getTile(testPos);
        if(testTile!=null && testTile.isOccupied())
        {
            if(testTile.getPiece().isDiffColor(isWhite) &&  testTile.getPiece() instanceof Pawn)
            {
                return true;
            }
        }
        testPos.setLocation(position.x-1, posY);
        testTile = boardInstance.getTile(testPos);
        if(testTile!=null && testTile.isOccupied())
        {
            return testTile.getPiece().isDiffColor(isWhite) && testTile.getPiece() instanceof Pawn;
        }
        return false;
    }

    /**
     * Vérification autour du Roi.
     * @param boardInstance La plateau actuel
     * @return Retourne Vrai s'il y a une piece qui menage le roi.
     */
    public boolean isAttackedAround(Board boardInstance, boolean isWhite){
        Point testPos;
        Tile testTile;
        int initPositions [][]= {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
        for (int[] initPosition : initPositions) {
            testPos = new Point(this.position.x + initPosition[0], this.position.y + initPosition[1]);
            testTile = boardInstance.getTile(testPos);
            if (testTile != null && testTile.isOccupied() && testTile.getPiece().isDiffColor(isWhite) && testTile.getPiece() instanceof King) {
                return true;
            }

        }
        return false;
    }

    /**
     * IsAttackHorizontallyAndVertically - Vérifie si une piece est attaqué ou non sur les axes H/V
     * @param boardInstance La plateau actuel
     * @param isWhite La couleur de la pièce
     * @return Retourne vrai si la piece est attaqué sinon faux
     */
    public boolean isAttackedHorizontallyAndVertically(Board boardInstance, boolean isWhite){

        int initPositions[][] = {{1,0}, {0,1}, {-1,0}, {0,-1}};
        Point testPos;
        Tile testTile;
        int modifierX, modifierY;

        for(int direction=0; direction < initPositions.length; direction++)
        {
            modifierX = 0;
            modifierY = 0;
            testPos = new Point(this.position.x + initPositions[direction][0],  this.position.y + initPositions[direction][1]);
            while(((testTile = boardInstance.getTile(testPos))!=null))
            {
                if (!testTile.isOccupied()) {

                    switch(direction) {
                        case 0: modifierX=1; break;
                        case 1: modifierY=1; break;
                        case 2: modifierX=-1; break;
                        case 3: modifierY=-1; break;
                    }
                    testPos = toCoord(new Point(testPos.x + modifierX, testPos.y + modifierY));
                }
                else {
                    if (testTile.getPiece().isDiffColor(isWhite) && (testTile.getPiece() instanceof Rook || testTile.getPiece() instanceof Queen)) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    /**
     * isAttackedOnDiagonal - Fonction vérifiant si une pièce est attaquée en diagonale.
     * @param boardInstance La plateau actuel
     * @param isWhite La couleur de la pièce
     * @return Retourne vrai si la pièce est attaquée sinon faux
     */
    public boolean isAttackedOnDiagonal(Board boardInstance, boolean isWhite){

        int initPositions [][]= {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        Point testPos;
        Tile testTile;
        int modifierX, modifierY;
        for(int i=0; i < initPositions.length; i++)
        {
            modifierX=0;
            modifierY=0;
            testPos = toCoord(new Point(this.position.x + initPositions[i][0],  this.position.y + initPositions[i][1]));
            while((testTile = boardInstance.getTile(testPos))!=null)
            {
                if (!testTile.isOccupied()) {

                    switch(i){
                        case 0: modifierX=1; modifierY=1; break;
                        case 1: modifierX=1; modifierY=-1; break;
                        case 2: modifierX=-1; modifierY=1; break;
                        case 3: modifierX=-1; modifierY=-1; break;
                    }
                    testPos = toCoord( new Point(testPos.x + modifierX,  testPos.y + modifierY));
                }
                else {
                    if (testTile.getPiece().isDiffColor(isWhite) && (testTile.getPiece() instanceof Bishop || testTile.getPiece() instanceof Queen)) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    /**
     * isAttackedByKnight - Fonction qui vérifie si une pièce est attaquée par un Knight
     * @param boardInstance La plateau actuel
     * @param isWhite Couleur de la pièce
     * @return Retourne vrai si la pièce est attaquée sinon faux
     */
    public boolean isAttackedByKnight(Board boardInstance, boolean isWhite){

        int initPositions [][]= {{1,2},{2,1},{1,-2},{-2,1},{-1,2},{2,-1},{-1,-2},{-2,-1}};
        Point testPos;
        Tile testTile;


        for (int[] initPosition : initPositions) {
            testPos = new Point(this.position.x + initPosition[0], this.position.y + initPosition[1]);
            testTile = boardInstance.getTile(testPos);
            if (testTile != null && testTile.isOccupied() && testTile.getPiece().isDiffColor(isWhite) && testTile.getPiece() instanceof Knight) {
                return true;
            }
        }
        return false;
    }

    /**
     * isAttacked - Fonction vérifiant si la pièce est attaquée.
     * @param boardInstance La plateau actuel
     * @param isWhite Couleur de la piece
     * @return Retourne vrai si la pièce est attaquée sinon faux
     */
    public boolean isAttacked(Board boardInstance, boolean isWhite){
        return isAttackedOnDiagonal(boardInstance, isWhite)
                ||isAttackedAround(boardInstance, isWhite)
                ||isAttackedByKnight(boardInstance,isWhite)
                ||isAttackedByPawn(boardInstance,isWhite)
                ||isAttackedHorizontallyAndVertically(boardInstance,isWhite);
    }
    /**
     * Permet de vérifier si la case est occupée
     * @return Vrai si une pièce existe sinon faux
     */
    public boolean isOccupied(){
        return piece!=null;
    }

    /**
     * Retourne la piece occupant la case
     * @return La pièce
     */
    public Piece getPiece(){
        return piece;
    }

    /**
     * Capture la piece de la case
     * @return Retourne son caractère de reconnaissance
     */
    public char capture()
    {
        char pieceType = ' ';
        if(isOccupied())
        {
            piece.setOnBoard(false);
            pieceType = piece.toShortName();
        }
        piece = null;
        return pieceType;
    }

    /**
     * Repose une pièce capturée pour la remise en arrière
     * @param pieceType Type de la pièce capturée
     */
    public void uncapture(Board board, char pieceType, boolean isWhite) throws UnsupportedOperationException
    {
        if(isOccupied())
            throw new UnsupportedOperationException("Case occupée");
        ArrayList<Piece> arr = board.searchPieces(pieceType, this.position, isWhite);
        for(Piece piece : arr)
        {
            if(!piece.getOnBoard())
            {
                this.piece = piece;
                this.piece.setOnBoard(true);
                break;
            }
        }
    }

    /**
     * Place une piece sur la case
     * @param piece Piece placée
     * @return caractère de la piece capturée si elle exsite, ' ' sinon
     */
    public char setPiece(Piece piece)
    {
        char capt = ' ';
        if(isOccupied())
            capt = capture();
        this.piece = piece;
        this.piece.setOnBoard(true);
        return capt;
    }

    public void moveOut()
    {
        this.piece = null;
    }



}
