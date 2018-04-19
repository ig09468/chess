package logique;


import ia.AIMovement;
import ia.EvaluationFunctionValues;
import layout.Controller;
import pieces.*;
import utils.ChessUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private long hash;

    public static final String[] defaultBoard = {
            "WRWNWBWQWKWBWNWR",
            "WPWPWPWPWPWPWPWP",
            "0000000000000000",
            "0000000000000000",
            "0000000000000000",
            "0000000000000000",
            "BPBPBPBPBPBPBPBP",
            "BRBNBBBQBKBBBNBR"
    };

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

    //Partie finie
    private boolean partieFinie;

    //Threefold Hash check
    private ArrayList<HashRecord> hashRecords;

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
        partieFinie=false;
        hashRecords=new ArrayList<>();

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
        hash = Controller.zobrist.calculateZobristHash(this);
    }

    public Board(Board board)
    {
        tileList = new Tile[8][8];
        pieceList= new ArrayList<>();

        boardState=null;
        whiteKing=null;
        blackKing=null;
        selectedPiece=board.selectedPiece;
        candidatPriseEnPassant=board.candidatPriseEnPassant != null ? (Point)board.candidatPriseEnPassant.clone() : null;
        needPromotion=null;
        isWhiteTurn=board.isWhiteTurn;
        partieFinie=board.partieFinie;
        hash= board.hash;
        hashRecords = new ArrayList<>(board.hashRecords.size());
        for(HashRecord hr : board.hashRecords) hashRecords.add(hr.clone());
        moveHistory=new ArrayList<>(board.moveHistory.size());
        for(MoveRecord mr : board.moveHistory) moveHistory.add(mr.clone());

        Piece piece;
        for(int x=0; x<8;x++)
        {
            for(int y=0; y<8;y++)
            {
                piece =null;
                Piece temp = board.getPiece(new Point(x,y));
                if(temp != null)
                {
                    if(temp instanceof Pawn)
                    {
                        piece = new Pawn(temp.isWhite(), new Point(x,y));
                    }else if(temp instanceof Rook)
                    {
                        piece = new Rook(temp.isWhite(), new Point(x,y));
                        piece.setHasNeverMoved(temp.getHasNeverMoved());
                    }else if(temp instanceof Bishop)
                    {
                        piece = new Bishop(temp.isWhite(), new Point(x,y));
                    }else if(temp instanceof Queen)
                    {
                        piece = new Queen(temp.isWhite(), new Point(x,y));
                    }else if(temp instanceof Knight)
                    {
                        piece = new Knight(temp.isWhite(), new Point(x,y));
                    }else if(temp instanceof King)
                    {
                        piece = new King(temp.isWhite(), new Point(x,y));
                        piece.setHasNeverMoved(temp.getHasNeverMoved());
                        if(temp.isWhite())
                        {
                            whiteKing = (King)piece;
                        }else
                        {
                            blackKing = (King)piece;
                        }
                    }
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

    public boolean move(Point oldPos, Point newPos){
        return move(oldPos, newPos, true, false);
    }

    public boolean move(Point oldPos, Point newPos, boolean promotionCheck, boolean isBuffer)
    {
        return move(oldPos, newPos, promotionCheck, isBuffer, null);
    }

    //Bouge une piece de son ancienne coordonnée vers une nouvelle
    public boolean move(Point oldPos, Point newPos, boolean promotionCheck, boolean isBuffer, Character promote){
        Tile oldTile = getTile(oldPos);
        Tile newTile = getTile(newPos);
        boolean castlerec = false;

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
                            this.moveHistory.add(MoveRecord.BIGCASTLE(oldPos, newPos, candidatPriseEnPassant));
                            castlerec = true;

                        }else if(((King) piece).getLittleCastle() && (oldPos.x-newPos.x) == -2)
                        {
                            this.moveWithoutCheck(new Point(7,piece.getPosition().y), new Point(5, piece.getPosition().y));
                            this.moveHistory.add(MoveRecord.SMALLCASTLE(oldPos, newPos, candidatPriseEnPassant));
                            castlerec = true;
                        }
                    }else if(promotionCheck && piece instanceof Pawn && newPos.y == (piece.isWhite() ? 7 : 0))
                    {
                        needPromotion = (Pawn)piece;
                        if(promote != null)
                        {
                            promote(promote);
                        }else
                        {
                            promoted = Controller.currentGame.askPromotion();
                        }
                    }
                }
                if(!enPassantrec && !castlerec)
                    this.moveHistory.add(new MoveRecord(oldPos, newPos, candidatPriseEnPassant,capt, promoted, neverMovedBefore, piece instanceof Pawn));
                candidatPriseEnPassant = (piece instanceof Pawn && Math.abs(oldPos.y - newPos.y) == 2) ? (Point)piece.getPosition().clone() : null;
                hash+=Controller.zobrist.getHashChange(moveHistory.get(moveHistory.size()-1),piece.toShortName(), isWhiteTurn, candidatPriseEnPassant);
                afterMove(isBuffer);
                return true;
            }else
            {
                return false;
            }
        }else
        {
            return false;
        }
    }

    public boolean move(Point oldPos, String newPos){
        return move(oldPos, ChessUtils.toCoord(newPos));
    }

    public boolean move(String oldPos, Point newPos){
        return move(ChessUtils.toCoord(oldPos), newPos);
    }

    public boolean move(String oldPos, String newPos){
        return move(ChessUtils.toCoord(oldPos), ChessUtils.toCoord(newPos));
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
            removeHashRecord();
            partieFinie=false;
            this.isWhiteTurn = !this.isWhiteTurn;
            MoveRecord rec = this.moveHistory.remove(this.moveHistory.size()-1);
            hash-=Controller.zobrist.getHashChange(rec,rec.getPromotion() != ' ' ? 'P' : getPiece(rec.getNewPos()).toShortName() , isWhiteTurn, candidatPriseEnPassant);
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
                    Tile captureTile = getTile(capturePos);
                    if(captureTile != null)
                    {
                        captureTile.uncapture(this, 'P', !isWhiteTurn);
                    }
                    newTile.getPiece().moveTo(oldTile, false, this);


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
                        }else
                        {
                            System.err.println("Piece doesn't exist");
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
            }else
            {
                System.err.println("Undo invalide");
            }
            //System.out.println("Undone : " + rec.getOldPos() +"->"+ rec.getNewPos()+ " : " +rec.getCapture());
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
        addHashRecord();
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
        if(partieFinie)
            return false;
        if(isInsufficientMaterial())
        {
            if(display)
            {
                Controller.staleMate(GameResult.MATERIAL);
            }
            partieFinie=true;
            return false;
        } else if(isThreefoldRule())
        {
            if(display)
            {
                Controller.staleMate(GameResult.THREEFOLD);
            }
            partieFinie=true;
            return false;
        } else if(isFiftyMovesRule())
        {
            if(display)
            {
                Controller.staleMate(GameResult.FIFTYMOVE);
            }
            partieFinie=true;
            return false;
        }
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
                        Controller.staleMate(GameResult.PAT);
                    }
                }else
                {
                    if(blackKing.isAttacked(this))
                    {
                        Controller.checkMate(true);
                    }else
                    {
                        Controller.staleMate(GameResult.PAT);
                    }
                }
            }
            partieFinie=true;
            return false;
        }
        partieFinie=false;
        return true;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public ArrayList<AIMovement> getAvailableMoves() {
        resetCalculatedLegalMoves();
        ArrayList<AIMovement> moves = new ArrayList<>();
        long killerMoveValue;
        for (Piece piece : pieceList) {
            if(piece.isOnBoard() && (piece.isWhite() == isWhiteTurn))
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

    public void fullUndo()
    {
        resetCalculatedLegalMoves();
        undo();
    }

    public Board clone()
    {
        return new Board(this);
    }

    public String moveHistoryToString()
    {
        StringBuilder sb = new StringBuilder();
        for(MoveRecord move : moveHistory)
        {
            sb.append(move).append(" ");
        }
        return sb.toString();
    }

    public boolean isFiftyMovesRule() //Un move, dans le jargon de la FIDE, est constitué des 2 traits
    {
        if(moveHistory.size() < 100)
            return false;
        int counter=0;
        for(int i=moveHistory.size()-1; i>=0 && counter <= 100; i--, counter++)
        {
            if(moveHistory.get(i).getIsPawnMove() ||moveHistory.get(i).getCapture() != ' ' || moveHistory.get(i).isPriseEnPassant())
                return false;
        }
        return true;
    }

    public boolean isInsufficientMaterial()
    {
        boolean bishopTileWhite=false;
        int nbBishop=0;
        boolean seenAKnight=false;
        for(Piece piece : pieceList)
        {
            if(piece.isOnBoard())
            {
                if(piece instanceof Bishop)
                {
                    if(seenAKnight) //Si il y a déjà un cavalier, il y a possibilité de mat
                        return false;
                    if(nbBishop==0)
                    {
                        bishopTileWhite=isLightTile(piece.getPosition());
                    }else if(isLightTile(piece.getPosition()) != bishopTileWhite) //Si des fous sont sur des cases de couleur différente, il y a possibilité de mat
                    {
                        return false;
                    }
                    nbBishop++;
                }else if(piece instanceof Knight)
                {
                    if(seenAKnight || nbBishop > 0) //Si il y a plus d'un cavalier sur le terrain, ou si il y a un fou, il y a possibilité de mat
                        return false;
                    seenAKnight=true;
                }else if(!(piece instanceof King)) //Si il y a une piece différente d'un fou, d'un cavalier ou d'un roi sur le terrain, il y a possibilité de mat
                {
                    return false;
                }
            }
        }
        //Si on a élucidé les autres hypothèses, alors il n'y a que des fous sur la même couleur de case, ou un seul cavalier, ou uniquement les rois, ce qui est insuffisant
        return true;
    }

    public boolean isThreefoldRule()
    {
        for(HashRecord hr : hashRecords)
        {
            if(hr.isThreefold())
                return true;
        }
        return false;
    }

    private boolean isLightTile(Point pos)
    {
        return pos.x%2 != pos.y%2;
    }

    public long getHash() {
        return hash;
    }

    private void addHashRecord()
    {
        HashRecord hr = new HashRecord(hash);
        if(hashRecords.isEmpty())
        {
            hashRecords.add(hr);
        }else
        {
            int index = Arrays.binarySearch(hashRecords.toArray(), hr);
            if(index < 0)
            {
                hashRecords.add(-(index+1), hr);
            }else
            {
                hashRecords.get(index).inc();
            }
        }
    }

    private void removeHashRecord()
    {
        HashRecord hr = new HashRecord(hash);
        if(!hashRecords.isEmpty())
        {
            int index = Arrays.binarySearch(hashRecords.toArray(), hr);
            if(index >= 0)
            {
                HashRecord ohr = hashRecords.get(index);
                ohr.dec();
                if(ohr.isNever())
                {
                    hashRecords.remove(index);
                }
            }

        }
    }
}
