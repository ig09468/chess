package ia;

import utils.ChessUtils;

import java.awt.*;
import java.util.Objects;

public class AIMovement {
    private long killerMoveValue;
    private Point from;
    private Point to;
    private char promotion;

    public AIMovement(Point from, Point to, char promotion, long killerMoveValue) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
        this.killerMoveValue = killerMoveValue;
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

    @Override
    public AIMovement clone()
    {
        return new AIMovement(from, to, promotion, killerMoveValue);
    }

    public String toString()
    {
        return ChessUtils.toStringPos(from)+ ChessUtils.toStringPos(to)+promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AIMovement that = (AIMovement) o;
        return promotion == that.promotion &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }
}
