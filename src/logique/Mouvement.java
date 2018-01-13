package logique;

import pieces.Pawn;
import utils.ChessUtils;

import java.awt.*;
import java.util.Arrays;

public class Mouvement {

    private Point newPos;
    private Point oldPos;
    private char capture;
    private char promotion;
    private boolean bigCastle;
    private boolean smallCastle;
    private boolean priseEnPassant;

    public static final Mouvement BIGCASTLE = new Mouvement(null, null, ' ', ' ', true, false, false);
    public static final Mouvement SMALLCASTLE = new Mouvement(null, null, ' ', ' ', false, true, false);

    private Mouvement(Point newPos, Point oldPos, char capture, char promotion, boolean isBigCastle, boolean isSmallCastle, boolean isPriseEnPassant) {
        this.newPos = newPos;
        this.oldPos = oldPos;
        this.capture = capture;
        this.promotion = promotion;
        this.bigCastle = isBigCastle;
        this.smallCastle = isSmallCastle;
        this.priseEnPassant = isPriseEnPassant;
    }

    public Mouvement(Point oldPos, Point newPos, char capture, char promotion)
    {
        this(oldPos, newPos, capture, promotion, false, false, false);
    }

    public Mouvement(Point oldPos, Point newPos, char capture)
    {
        this(oldPos, newPos, capture, ' ');
    }

    public Mouvement(Point oldPos, Point newPos)
    {
        this(oldPos, newPos, ' ');
    }

    public static Mouvement priseEnPassant(Point oldPos, Point newPos)
    {
        return new Mouvement(oldPos, newPos, ' ', ' ', false, false, true);
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
