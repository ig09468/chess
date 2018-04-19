package logique;

import utils.ChessUtils;

import java.awt.*;
import java.util.Arrays;

public class MoveRecord {


    private Point oldPos;
    private Point newPos;
    private char capture;
    private char promotion;
    private boolean bigCastle;
    private boolean smallCastle;
    private boolean priseEnPassant;
    private boolean hasNeverMoved;
    private Point enPassantCandidate;
    private boolean isPawnMove;

    public static MoveRecord BIGCASTLE(Point oldPos, Point newPos,Point enPassantCandidate)
    {
        return new MoveRecord(oldPos, newPos, enPassantCandidate,' ', ' ', true, false, false, true, false);
    }
    public static MoveRecord SMALLCASTLE(Point oldPos, Point newPos,Point enPassantCandidate)
    {
        return new MoveRecord(oldPos, newPos, enPassantCandidate, ' ', ' ', false, true, false, true, false);
    }

    private MoveRecord(Point oldPos, Point newPos, Point enPassantCandidate, char capture, char promotion, boolean isBigCastle, boolean isSmallCastle, boolean isPriseEnPassant, boolean hasNeverMoved, boolean isPawnMove) {
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.capture = capture;
        this.promotion = promotion;
        this.bigCastle = isBigCastle;
        this.smallCastle = isSmallCastle;
        this.priseEnPassant = isPriseEnPassant;
        this.hasNeverMoved = hasNeverMoved;
        this.enPassantCandidate = enPassantCandidate;
        this.isPawnMove = isPawnMove;
    }

    public MoveRecord(Point oldPos, Point newPos, Point enPassantCandidate,char capture, char promotion, boolean hasNeverMoved, boolean isPawnMove)
    {
        this(oldPos, newPos, enPassantCandidate, capture, promotion, false, false, false, hasNeverMoved, isPawnMove);
    }

    public MoveRecord(Point oldPos, Point newPos, Point enPassantCandidate, char capture, boolean hasNeverMoved, boolean isPawnMove)
    {
        this(oldPos, newPos, enPassantCandidate, capture, ' ', hasNeverMoved, isPawnMove);
    }

    public MoveRecord(Point oldPos, Point newPos, Point enPassantCandidate, boolean hasNeverMoved, boolean isPawnMove)
    {
        this(oldPos, newPos, enPassantCandidate, ' ', hasNeverMoved, isPawnMove);
    }

    public static MoveRecord priseEnPassant(Point oldPos, Point newPos, Point enPassantCandidate)
    {
        return new MoveRecord(oldPos, newPos, enPassantCandidate, ' ', ' ', false, false, true, false, true);
    }

    public Point getNewPos() {
        return newPos;
    }

    public Point getOldPos() {
        return oldPos;
    }

    public Point getEnPassantCandidate() {
        return enPassantCandidate;
    }

    public char getCapture() {
        return capture;
    }

    public char getPromotion() {
        return promotion;
    }

    public boolean isBigCastle() {
        return bigCastle;
    }

    public boolean isSmallCastle() {
        return smallCastle;
    }

    public boolean isPriseEnPassant() {
        return priseEnPassant;
    }

    public boolean hasNeverMoved(){return hasNeverMoved;}

    public String toString()
    {
        if(bigCastle)
            return "0-0-0";
        if(smallCastle)
            return "0-0";
        try
        {
            StringBuilder sb = new StringBuilder(ChessUtils.toStringPos(oldPos));
            if(capture != ' ')
            {
                sb.append("x");
                if(!priseEnPassant)
                    sb.append(capture);
            }
            sb.append(ChessUtils.toStringPos(newPos));
            if(promotion != ' ')
                sb.append(promotion);
            if(priseEnPassant)
                sb.append("e.p");
            return sb.toString();
        }catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            return "";
        }

    }

    public boolean getIsPawnMove() {
        return isPawnMove;
    }

    @Override
    protected MoveRecord clone(){
        return new MoveRecord((Point)oldPos.clone(), (Point)newPos.clone(), (Point)enPassantCandidate.clone(), capture, promotion, bigCastle, smallCastle, priseEnPassant, hasNeverMoved, isPawnMove);
    }
}
