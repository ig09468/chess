package logique;

import utils.ChessUtils;

import java.awt.*;
import java.util.Arrays;

public class MoveRecord {

    private Point newPos;
    private Point oldPos;
    private char capture;
    private char promotion;
    private boolean bigCastle;
    private boolean smallCastle;
    private boolean priseEnPassant;
    private boolean hasNeverMoved;

    public static final MoveRecord BIGCASTLE = new MoveRecord(null, null, ' ', ' ', true, false, false, true);
    public static final MoveRecord SMALLCASTLE = new MoveRecord(null, null, ' ', ' ', false, true, false, true);

    private MoveRecord(Point newPos, Point oldPos, char capture, char promotion, boolean isBigCastle, boolean isSmallCastle, boolean isPriseEnPassant, boolean hasNeverMoved) {
        this.newPos = newPos;
        this.oldPos = oldPos;
        this.capture = capture;
        this.promotion = promotion;
        this.bigCastle = isBigCastle;
        this.smallCastle = isSmallCastle;
        this.priseEnPassant = isPriseEnPassant;
        this.hasNeverMoved = hasNeverMoved;
    }

    public MoveRecord(Point oldPos, Point newPos, char capture, char promotion, boolean hasNeverMoved)
    {
        this(oldPos, newPos, capture, promotion, false, false, false, hasNeverMoved);
    }

    public MoveRecord(Point oldPos, Point newPos, char capture, boolean hasNeverMoved)
    {
        this(oldPos, newPos, capture, ' ', hasNeverMoved);
    }

    public MoveRecord(Point oldPos, Point newPos, boolean hasNeverMoved)
    {
        this(oldPos, newPos, ' ', hasNeverMoved);
    }

    public static MoveRecord priseEnPassant(Point oldPos, Point newPos)
    {
        return new MoveRecord(oldPos, newPos, ' ', ' ', false, false, true, false);
    }

    public Point getNewPos() {
        return newPos;
    }

    public Point getOldPos() {
        return oldPos;
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
            StringBuilder sb = new StringBuilder(ChessUtils.pointToString(oldPos));
            if(capture != ' ')
            {
                sb.append("x");
                if(!priseEnPassant)
                    sb.append(capture);
            }
            sb.append(newPos);
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
}
