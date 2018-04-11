package logique;


import ia.AIMovement;
import ia.EvaluationFunctionValues;
import layout.Controller;
import pieces.*;
import utils.ChessUtils;

import java.awt.*;
import java.util.ArrayList;

public class Board {

    public static String[] defaultBoard = {
            "WRWNWBWQWKWBWNWR",
            "WPWPWPWPWPWPWPWP",
            "0000000000000000",
            "0000000000000000",
            "0000000000000000",
            "0000000000000000",
            "BPBPBPBPBPBPBPBP",
            "BRBNBBBQBKBBBNBR"
    };

    //Cases sélectionnées
    private ArrayList<Tile> selectedTiles;

    //Pièce sélectionée
    private Piece selectedPiece;

    //Booléen désignant si c'est le tour des blancs
    private boolean isWhiteTurn;

    //Historique des mouvements
    public ArrayList<MoveRecord> moveHistory;

    //Liste des tiles
    private Tile[][] tileList;

    //Liste des pièces
    private ArrayList<Piece> pieceList;

    //Rois
    private King whiteKing;
    private King blackKing;

    //Etat actuel
    private ArrayList<Byte> boardState;

    //Candidat prise en passant
    private Point candidatPriseEnPassant;

    //Pion necessitant une promotion
    private Pawn needPromotion;

    public Board()
    {
        tileList = new Tile[8][8];
        pieceList= new ArrayList<>();
        moveHistory=new ArrayList<>();
        boardState=null;
        whiteKing=null;
        blackKing=null;
        candidatPriseEnPassant=null;
        needPromotion=null;
        isWhiteTurn=true;

        boolean isWhite;
        Piece piece;
        for(int x=0; x<8;x++)
        {
            for(int y=0; y<8;y++)
            {
                isWhite = defaultBoard[y].charAt(2*x) == 'W';
                switch(defaultBoard[y].charAt(2*x+1))
                {
                    case Bishop.SHORTNAME:
                        piece = new Bishop(isWhite, new Point(x,y));
                        break;
                    case Rook.SHORTNAME:
                        piece = new Rook(isWhite, new Point(x,y));
                        break;
                    case Queen.SHORTNAME:
                        piece = new Queen(isWhite, new Point(x,y));
                        break;
                    case King.SHORTNAME:
                        piece = new King(isWhite, new Point(x,y));
                        if(isWhite)
                        {
                            whiteKing=(King)piece;
                        }else
                        {
                            blackKing=(King)piece;
                        }
                        break;
                    case Pawn.SHORTNAME:
                        piece = new Pawn(isWhite, new Point(x,y));
                        break;
                    case Knight.SHORTNAME:
                        piece = new Knight(isWhite, new Point(x,y));
                        break;
                    default:
                        piece = null;
                        break;
                }
                tileList[x][y] = new Tile(new Point(x,y),piece);
                if(piece != null)
                {
                    pieceList.add(piece);
                }
            }
        }
    }

    public boolean compareToEnPassantCandidat(Pawn piece)
    {
        return piece.getPosition().equals(candidatPriseEnPassant);
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
        move(oldPos, newPos, true, false);
    }

    //Bouge une piece de son ancienne coordonée vers une nouvelle
    public void move(Point oldPos, Point newPos, boolean promotionCheck, boolean isBuffer)
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
                boolean enPassantrec = piece instanceof Pawn && this.enPassant(oldTile, newTile, ((Pawn) piece).getCapturePos(newPos.x));
                char capt = piece.moveTo(newTile, false, this);
                oldTile.moveOut();
                char promoted = ' ';
                if(piece instanceof Pawn && enPassantrec)
                {
                    this.moveHistory.add(MoveRecord.priseEnPassant(oldPos, newPos, candidatPriseEnPassant));
                }
                else
                {
                    if(piece instanceof King)
                    {
                        if(((King) piece).getBigCastle() && (oldPos.x-newPos.x) == 2)
                        {
                            this.moveWithoutCheck(new Point(0,piece.getPosition().y), new Point(3, piece.getPosition().y));
                            this.moveHistory.add(MoveRecord.BIGCASTLE(candidatPriseEnPassant));
                        }else if(((King) piece).getLittleCastle() && (oldPos.x-newPos.x) == -2)
                        {
                            this.moveWithoutCheck(new Point(7,piece.getPosition().y), new Point(5, piece.getPosition().y));
                            this.moveHistory.add(MoveRecord.SMALLCASTLE(candidatPriseEnPassant));
                        }
                    }else if(promotionCheck && piece instanceof Pawn && newPos.y == (piece.isWhite() ? 7 : 0))
                    {
                        needPromotion = (Pawn)piece;
                        promoted = Controller.currentGame.askPromotion();
                    }
                }
                if(!enPassantrec)
                    this.moveHistory.add(new MoveRecord(oldPos, newPos, candidatPriseEnPassant,capt, promoted, neverMovedBefore));
                candidatPriseEnPassant = (piece instanceof Pawn && Math.abs(oldPos.y - newPos.y) == 2) ? (Point)piece.getPosition().clone() : null;
                afterMove(isBuffer);

            }
        }else
        {
            System.err.println("Wrong move : "+oldPos + "->" + newPos);
            for(MoveRecord move : moveHistory)
            {
                System.err.println(move.getOldPos()+"->"+move.getNewPos()+":"+move.getCapture()+" ; ");
            }
            System.err.println();
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
            oldTile.getPiece().moveTo(newTile, false, this);
        }
    }

    //Annule le dernier mouvement
    public void undo()
    {
        if(this.moveHistory.size() > 0)
        {
            this.isWhiteTurn = !this.isWhiteTurn;
            MoveRecord rec = this.moveHistory.remove(this.moveHistory.size()-1);
            Tile oldTile = getTile(rec.getOldPos());
            Tile newTile = getTile(rec.getNewPos());
            if(oldTile!= null && newTile != null && newTile.isOccupied() && !oldTile.isOccupied())
            {
                if(rec.isBigCastle())
                {
                    int posy = rec.getOldPos().y;
                    Tile rookTile = getTile(new Point(3, posy));
                    Tile originalRookTile = getTile(new Point(0, posy));
                    if(rookTile != null && rookTile.isOccupied() && originalRookTile != null)
                    {
                        newTile.getPiece().moveTo(oldTile, true, this);
                        rookTile.getPiece().moveTo(originalRookTile, true, this);
                    }
                }else if(rec.isSmallCastle())
                {
                    int posy = rec.getOldPos().y;
                    Tile rookTile = getTile(new Point(5, posy));
                    Tile originalRookTile = getTile(new Point(7, posy));
                    if(rookTile != null && rookTile.isOccupied() && originalRookTile != null)
                    {
                        newTile.getPiece().moveTo(oldTile, true, this);
                        rookTile.getPiece().moveTo(originalRookTile, true, this);
                    }
                }else if(rec.isPriseEnPassant())
                {
                    int posx = rec.getNewPos().x;
                    Point capturePos = new Point(posx, rec.getOldPos().y);
                    newTile.getPiece().moveTo(oldTile, false, this);
                    Tile captureTile = getTile(capturePos);
                    if(captureTile != null)
                    {
                        captureTile.uncapture(this, 'P', !isWhiteTurn);
                    }
                }else
                {
                    Piece piece = newTile.getPiece();

                    if(rec.getPromotion() != ' ')
                    {
                        int pieceListIndex = pieceList.indexOf(piece);
                        piece = new Pawn(piece.isWhite(), piece.getPosition());
                        if(pieceListIndex!=-1)
                        {
                            pieceList.set(pieceListIndex, piece);
                            piece.moveTo(oldTile, false, this);

                        }
                    }else
                    {
                        piece.moveTo(oldTile, rec.hasNeverMoved(), this);
                    }

                    if(rec.getCapture() != ' ')
                    {
                        newTile.uncapture(this, rec.getCapture(), !isWhiteTurn);
                    }
                }
                candidatPriseEnPassant = rec.getEnPassantCandidate();
            }
            //System.out.println("Undone : " + mr.getOldPos() +"->"+ mr.getNewPos()+ " : " +mr.getCapture());
            this.boardState = null;
        }else
        {
            System.err.println("Undo vide");
        }
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
        ArrayList<Piece> pieces = new ArrayList<>();
        for(Piece piece : pieceList)
        {
            if(piece.toShortName() == pieceType)
                pieces.add(piece);
        }
        return pieces;
    }

    public ArrayList<Piece> searchPieces(char pieceType, Point pos, boolean isWhite)
    {
        ArrayList<Piece> pieces = new ArrayList<>();
        for(Piece piece : pieceList)
        {
            if(piece.toShortName() == pieceType && pos.equals(piece.getPosition()) && piece.isWhite()==isWhite)
                pieces.add(piece);
        }
        return pieces;
    }

    public boolean enPassant(Tile oldTile, Tile newTile, Point enPassantCapturePos)
    {

        if(enPassantCapturePos != null && oldTile != null && newTile != null)
        {

            Tile captureTile = this.getTile(enPassantCapturePos);
            if(captureTile != null && !newTile.isOccupied() && captureTile.getPiece() instanceof Pawn && this.compareToEnPassantCandidat((Pawn)captureTile.getPiece()))
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

    private void afterMove(boolean isBuffer)
    {
        this.boardState = null;
        isWhiteTurn = !isWhiteTurn;
        if(!isBuffer)
        {
            resetCalculatedLegalMoves();
            Controller.showGame();
            this.calculateStatus();
        }
        this.selectedPiece = null;
    }

    public void resetCalculatedLegalMoves()
    {
        this.pieceList.forEach(Piece::resetLegalMoves);
    }

    public boolean calculateStatus()
    {
        return calculateStatus(true);
    }

    public boolean calculateStatus(boolean display)
    {
        //TODO Ajouter la regle des 50 coups et le nul par materiel insuffisant
        Board that = this;
        Piece[] pieceArray = new Piece[pieceList.size()];
        pieceList.toArray(pieceArray);
        if(ChessUtils.testForAll(pieceArray, (Piece piece)->{
            if(piece.isDiffColor(isWhiteTurn))
                return true;
            else
            {
                if(piece.isOnBoard())
                {
                    piece.calculateLegalMoves(that);
                    return !piece.canMove();
                }else
                    return true;
            }
        }))
        {
            if(display)
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
            }
            return false;
        }
        return true;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public ArrayList<AIMovement> getAvailableMoves() {
        ArrayList<AIMovement> moves = new ArrayList<>();
        long killerMoveValue;
        for (Piece piece : pieceList) {
            if(piece.isOnBoard() && piece.isWhite() == isWhiteTurn)
            {
                Point pos = piece.getPosition();
                piece.calculateLegalMoves(this);
                ArrayList<Point> legalMoves = piece.getLegalMoves();
                for(Point to: legalMoves)
                {
                    Piece toPiece = getPiece(to);
                    if(toPiece != null)
                    {
                        int index = toPiece.toShortName()-'B';
                        if(index >=0 && index < EvaluationFunctionValues.pieceValues.length)
                        {
                            killerMoveValue = EvaluationFunctionValues.pieceValues[index];
                        }else
                        {
                            killerMoveValue=0;
                        }
                    }else
                    {
                        killerMoveValue = 0;
                    }
                    if(piece instanceof Pawn && to.y == (piece.isWhite() ? 7 : 0))
                    {
                        moves.add(new AIMovement(pos, to, Queen.SHORTNAME,killerMoveValue));
                        moves.add(new AIMovement(pos, to, Bishop.SHORTNAME,killerMoveValue));
                        moves.add(new AIMovement(pos, to, Rook.SHORTNAME,killerMoveValue));
                        moves.add(new AIMovement(pos, to, Knight.SHORTNAME,killerMoveValue));
                    }else
                    {
                        moves.add(new AIMovement(pos, to, killerMoveValue));
                    }
                }
            }
        }
        return moves;
    }

    public long getValue()
    {
        int index;
        long value=0;
        for(Piece piece : pieceList)
        {
            if(piece.isOnBoard())
            {
                index = piece.toShortName() - 'B';
                if(index >=0 && index < EvaluationFunctionValues.pieceValues.length)
                {
                    if(piece.isWhite())
                    {
                        value+=EvaluationFunctionValues.pieceValues[index];
                    }else
                    {
                        value-=EvaluationFunctionValues.pieceValues[index];
                    }
                }
            }
        }
        return value;
    }

    public MoveRecord getLastMove()
    {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size()-1);
    }

    public void getBoardRepresentation()
    {
        for(int x = 7; x>=0;x--)
        {
            for(int y=0; y<8;y++)
            {
                Piece piece = getPiece(new Point(x,y));
                System.err.print(piece == null ? "  ," : piece.toShortName()+(piece.isWhite() ? "W" : "B")+",");
            }
            System.err.println();
        }
    }
}
