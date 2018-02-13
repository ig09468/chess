package logique;


import layout.Controller;
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

    //Candidat prise en passant
    private Pawn candidatPriseEnPassant = null;

    //Pion necessitant une promotion
    private Pawn needPromotion = null;

    public Board()
    {

    }

    public boolean compareToEnPassantCandidat(Pawn piece)
    {
        return piece == candidatPriseEnPassant;
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

    public boolean promotionNeeded()
    {
        return needPromotion!= null;
    }

    public boolean isWhiteTurn()
    {
        return isWhiteTurn;
    }

    public void move(Point oldPos, Point newPos)
    {
        move(oldPos, newPos, true);
    }

    //Bouge une piece de son ancienne coordonée vers une nouvelle
    public void move(Point oldPos, Point newPos, boolean promotionCheck)
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
                boolean neverMovedBefore = piece.getHasNeverMoved();
                char capt = piece.moveTo(newTile);
                char promoted = ' ';
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
                            this.moveWithoutCheck(new Point(0,piece.getPosition().y), new Point(3, piece.getPosition().y));
                            this.moveHistory.add(MoveRecord.BIGCASTLE);
                        }else if(((King) piece).getLittleCastle() && (oldPos.x-newPos.x) == -2)
                        {
                            this.moveWithoutCheck(new Point(7,piece.getPosition().y), new Point(5, piece.getPosition().y));
                            this.moveHistory.add(MoveRecord.SMALLCASTLE);
                        }
                    }else if(promotionCheck && piece instanceof Pawn && newPos.y == (piece.isWhite() ? 7 : 0))
                    {
                        needPromotion = (Pawn)piece;
                        promoted = Controller.currentGame.askPromotion();
                    }
                }
                this.moveHistory.add(new MoveRecord(oldPos, newPos, capt, promoted, neverMovedBefore));
                candidatPriseEnPassant = (piece instanceof Pawn && Math.abs(oldPos.y - newPos.y) == 2) ? (Pawn)piece : null;
                afterMove();

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
        Tile oldTile = getTile(oldPos);
        Tile newTile = getTile(newPos);
        if(oldTile != null && oldTile.isOccupied() && newTile != null)
        {
            oldTile.getPiece().moveTo(newTile);
        }
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
            if(captureTile != null && !newTile.isOccupied() && captureTile.getPiece() == candidatPriseEnPassant)
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
            Pawn -          1W1IsEnPassantCandidat
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
                        toggle += (compareToEnPassantCandidat((Pawn)piece)) ? 3 : 2;
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

    public char promote(char piece)
    {
        if(promotionNeeded())
        {
            Piece newPiece;
            boolean pieceColorWhite = needPromotion.isWhite();
            Point position = needPromotion.getPosition();
            Tile promTile = getTile(position);
            int pieceListIndex = pieceList.indexOf(needPromotion);
            if(promTile != null && promTile.isOccupied() && pieceListIndex!= -1)
            {
                switch(piece)
                {
                    case Bishop.SHORTNAME:
                        newPiece = new Bishop(pieceColorWhite, position);
                        break;
                    case Rook.SHORTNAME:
                        newPiece = new Rook(pieceColorWhite, position);
                        break;
                    case Queen.SHORTNAME:
                        newPiece = new Queen(pieceColorWhite, position);
                        break;
                    case Knight.SHORTNAME:
                        newPiece = new Knight(pieceColorWhite, position);
                        break;
                    default:
                        return ' ';
                }
                promTile.setPiece(newPiece);
                pieceList.set(pieceListIndex, newPiece);
                return piece;
            }
        }
        return ' ';
    }

    private void afterMove()
    {
        this.boardState = null;
        Controller.showGame();
        this.isWhiteTurn = !this.isWhiteTurn;
        this.calculateStatus();
    }

    public boolean calculateStatus()
    {
        Board that = this;
        if(ChessUtils.testForAll((Piece[])pieceList.toArray(), (Piece piece)->{
            if(piece.isDiffColor(isWhiteTurn))
                return true;
            else
            {
                if(piece.isOnBoard())
                {
                    piece.calculateLegalMoves(that);
                    return piece.canMove();
                }else
                    return false;
            }
        }))
        {
            if(isWhiteTurn)
            {
                if(whiteKing.isAttacked(this))
                {
                    Controller.checkMate(false);
                }else
                {
                    Controller.staleMate();
                }
            }else
            {
                if(blackKing.isAttacked(this))
                {
                    Controller.checkMate(true);
                }else
                {
                    Controller.staleMate();
                }
            }
            return false;
        }
        return true;
    }

}
