package logique;


import pieces.*;
import utils.ChessUtils;

import java.awt.*;
import java.util.ArrayList;

public class Board {

    //Cases sélectionnées
    private ArrayList<Tile> selectedTiles;

    //Pièce sélectionée
    private Piece selectedPiece;

    //Booléen désignant si le plateau a changé depuis la dernière évaluation de son état
    private boolean changed;

    //Etat du plateau
    private String etat;

    //Booléen désignant le plateau comme étant en buffer ou non
    private boolean isBuffer;

    //Booléen désignant si c'est le tour des blancs
    private boolean isWhiteTurn;

    //Historique des mouvements
    private ArrayList<MoveRecord> moveHistory;

    //Liste des tiles
    private Tile[][] tileList = new Tile[8][8];

    //Liste des pièces
    private ArrayList<Piece> pieceList;

    //Rois
    private King whiteKing = null;
    private King blackKing = null;

    //Etat actuel
    ArrayList<Byte> boardState;

    public Board()
    {

    }

    //Récupère la case aux coordonnées spécifiées
    public Tile getTile(Point position){
        position = ChessUtils.toCoord(position);
        return position != null ? tileList[position.x][position.y] : null;
    }

    public Tile getTile(String position)
    {
        return getTile(ChessUtils.toCoord(position));
    }

    //Bouge une piece de son ancienne coordonée vers une nouvelle
    public void move(Point oldPos, Point newPos)
    {
        Tile oldTile = getTile(oldPos);
        Tile newTile = getTile(newPos);

        if(oldTile != null && newTile != null && oldTile.isOccupied())
        {
            Piece piece = oldTile.getPiece();
            if(!piece.isLegalMovesCalculated())
            {
                piece.calculateLegalMoves(this);
            }
            if(piece.isLegalMove(newPos))
            {
                char capt = piece.moveTo(newTile);
                if(piece instanceof Pawn && this.enPassant(oldTile, newTile, ((Pawn)piece).getCapturePos(newPos.x)))
                {
                    this.moveHistory.add(MoveRecord.priseEnPassant(oldPos, newPos));
                }
                else
                {
                    if(piece instanceof King)
                    {
                        if(((King) piece).getBigCastle() && (oldPos.x-newPos.x) == 2)
                        {

                        }
                    }
                }

            }


        }
    }

    public void move(Point oldPos, String newPos)
    {
        move(oldPos, ChessUtils.toCoord(newPos));
    }

    public void move(String oldPos, Point newPos)
    {
        move(ChessUtils.toCoord(oldPos), newPos);
    }

    public void move(String oldPos, String newPos)
    {
        move(ChessUtils.toCoord(oldPos), ChessUtils.toCoord(newPos));
    }

    public void moveWithoutCheck(Point oldPos, Point newPos)
    {

    }

    //Annule le dernier mouvement
    public void undo()
    {

    }

    //Récupère le roi de la couleur spécifiée
    public King getKing(boolean isWhite)
    {
        return isWhite ? whiteKing : blackKing;
    }

    public Piece getPiece(Point coord)
    {
        coord = ChessUtils.toCoord(coord);
        if(coord != null)
        {
            Tile tile = getTile(coord);
            if(tile !=null)
                return tile.getPiece();
        }
        return null;
    }

    public Piece getPiece(String coord)
    {
        return getPiece(ChessUtils.toCoord(coord));
    }

    public ArrayList<Piece> searchPieces(Point pos)
    {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(Piece piece : pieceList)
        {
            if(pos.equals(piece.getPosition()))
                pieces.add(piece);
        }
        return pieces;
    }

    public ArrayList<Piece> searchPieces(char pieceType)
    {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(Piece piece : pieceList)
        {
            if(piece.toShortName() == pieceType)
                pieces.add(piece);
        }
        return pieces;
    }

    public ArrayList<Piece> searchPieces(char pieceType, Point pos)
    {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(Piece piece : pieceList)
        {
            if(piece.toShortName() == pieceType && pos.equals(piece.getPosition()))
                pieces.add(piece);
        }
        return pieces;
    }

    public boolean enPassant(Tile oldTile, Tile newTile, Point enPassantCapturePos)
    {
        if(enPassantCapturePos != null && oldTile != null && newTile != null)
        {
            Tile captureTile = this.getTile(enPassantCapturePos);
            if(captureTile != null && !newTile.isOccupied() && captureTile.getPiece() instanceof Pawn && ((Pawn) captureTile.getPiece()).getLastMoveIsDouble())
            {
                captureTile.capture();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Byte> getBoardState() {
        if(boardState == null)
        {
            boardState = toBoardRecord();
        }
        return boardState;
    }

    public ArrayList<Byte> toBoardRecord()
    {
        /*Record :
            24 octets max
            Case vide -     0
            Pawn -          1W1LastMoveIsDouble
            Rook -          1W01HasNeverMoved
            Bishop -        1W0010
            Knight -        1W0011
            Queen -         1W0001
            King -          1W0000HasNeverMoved
        */

        short current=0;
        short copy;
        int cursorCurrent=0;
        int cursorInc=0;
        ArrayList<Byte> bytes = new ArrayList<>();
        Point tilePos = new Point(0,0);
        Tile tile;
        for(int i=0; i<8;i++)
        {
            for(int j=0; j<8;j++)
            {
                tilePos.x = i;
                tilePos.y = j;
                tile = this.getTile(tilePos);
                short toggle = 1;
                if(tile.isOccupied())
                {
                    Piece piece = tile.getPiece();
                    toggle = piece.isWhite() ? (short)3 : (short)2;
                    if(piece instanceof Pawn)
                    {
                        toggle <<= 2;
                        toggle += ((Pawn)piece).getLastMoveIsDouble() ? 3 : 2;
                        cursorInc = 4;
                    }else if(piece instanceof Rook)
                    {
                        toggle <<= 3;
                        toggle += piece.getHasNeverMoved() ? 3 : 2;
                        cursorInc = 5;
                    }else if(piece instanceof Bishop)
                    {
                        toggle <<=4;
                        toggle += 2;
                        cursorInc=6;
                    }else if(piece instanceof Knight)
                    {
                        toggle <<=4;
                        toggle += 3;
                        cursorInc=6;
                    }else if(piece instanceof Queen)
                    {
                        toggle <<=4;
                        toggle += 1;
                        cursorInc=6;
                    }else if(piece instanceof King)
                    {
                        toggle <<=5;
                        if(piece.getHasNeverMoved())
                            toggle+=1;
                        cursorInc=7;
                    }
                    toggle <<= 16 - cursorInc - cursorCurrent;
                    current |= toggle;
                }else
                {

                    toggle <<= 15-cursorCurrent;
                    toggle = (short)~toggle;
                    current &= toggle;
                    cursorInc=1;
                }
                cursorCurrent += cursorInc;
                if(cursorCurrent>=8)
                {
                    copy = current;
                    copy >>= 8;
                    bytes.add((byte)copy);
                    current <<=8;
                    cursorCurrent-=8;
                }
            }

        }
        return bytes;
    }
}
