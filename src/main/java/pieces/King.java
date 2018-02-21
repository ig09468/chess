package pieces;

import logique.Board;
import logique.Tile;
import utils.ChessUtils;

import java.awt.*;

import static utils.ChessUtils.testForAll;
import static utils.ChessUtils.toCoord;

/** Classe pour les rois
 * @extends Piece
 *
 * */
public class King extends Piece {

    private boolean littleCastle;

    private boolean bigCastle;

    public static final char SHORTNAME = 'K';
    /**
     * Constructeur de la classe
     * @param color
     * @param position
     */
    public King(boolean color, Point position){
        super(color,position);
    }

    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){
        if (boardInstance == null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }

        this.legalMoves.clear();

        int initPositions[][] = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        legalMovesCalculatedForKnightAndKing(boardInstance,initPositions);
        Point testPos;
        this.legalMovesCalculated = true;
        this.decimateLegalMovesCheck(boardInstance);
        if((testPos = this.checkBigCastle(boardInstance))!=null)
        {
            this.legalMoves.add((Point)testPos.clone());
            this.bigCastle = true;
        }
        if((testPos = this.checkLittleCastle(boardInstance))!=null)
        {
            this.legalMoves.add((Point)testPos.clone());
            this.littleCastle = true;
        }

    }


    public boolean getLittleCastle(){
        return this.littleCastle;
    }

    public boolean getBigCastle(){
        return this.bigCastle;
    }

    public static String toShortString(){
        return "K";
    }

    public Point checkLittleCastle(Board boardInstance)
    {
        if(this.hasNeverMoved && !this.isAttacked(boardInstance))
        {
            int row = this.white ? 0 : 7;
            Tile rookTile = boardInstance.getTile(new Point(7,row));
            if(rookTile != null && rookTile.isOccupied() && rookTile.getPiece() instanceof Rook && rookTile.getPiece().getHasNeverMoved())
            {
                Tile[] testTiles = {boardInstance.getTile(new Point(6,row)), boardInstance.getTile(new Point(5, row))};
                if(testForAll(testTiles, (t)-> t != null && !t.isOccupied()))
                {
                    if(!testTiles[0].isAttacked(boardInstance, this.white) && !testTiles[1].isAttacked(boardInstance, this.white))
                        return testTiles[0].getPosition();
                }
            }
        }
        return null;
    }

    public Point checkBigCastle(Board boardInstance)
    {
        if(this.hasNeverMoved && !this.isAttacked(boardInstance))
        {
            int row = this.white ? 0 : 7;
            Tile rookTile = boardInstance.getTile(new Point(0,row));
            if(rookTile != null && rookTile.isOccupied() && rookTile.getPiece() instanceof Rook && rookTile.getPiece().getHasNeverMoved())
            {
                Tile[] testTiles = {boardInstance.getTile(new Point(1,row)), boardInstance.getTile(new Point(2, row)), boardInstance.getTile(new Point(3, row))};
                if(testForAll(testTiles, (t)-> t != null && !t.isOccupied()))
                {
                    if(!testTiles[1].isAttacked(boardInstance, this.white) && !testTiles[2].isAttacked(boardInstance, this.white))
                    return testTiles[1].getPosition();
                }
            }
        }
        return null;
    }

    public boolean wouldBeInCheck(Board boardInstance, Point oldPos, Point newPos) throws IllegalArgumentException
    {
        oldPos = ChessUtils.toCoord(oldPos);
        newPos = ChessUtils.toCoord(newPos);
        if(boardInstance == null || oldPos == null || newPos == null)
            throw new IllegalArgumentException();
        boardInstance.move(oldPos, newPos);
        boolean check = this.isAttacked(boardInstance);
        boardInstance.undo();
        return check;
    }

    public char toShortName(){ return SHORTNAME; }
}
