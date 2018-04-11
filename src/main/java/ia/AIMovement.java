package ia;

import java.awt.*;

public class AIMovement {
    private long killerMoveValue;
    private Point from;
    private Point to;
    private char promotion;

    public AIMovement(Point from, Point to, char promotion, long killerMoveValue) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public AIMovement(Point from, Point to, long killerMoveValue)
    {
        this(from, to ,' ',killerMoveValue);
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    public char getPromotion() {
        return promotion;
    }

    public long getKillerMoveValue() {
        return killerMoveValue;
    }
}
