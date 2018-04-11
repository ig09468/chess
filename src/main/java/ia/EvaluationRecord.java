package ia;

import java.awt.*;
import java.util.Objects;

public class EvaluationRecord {
    private long zobristHash;
    private long alpha;
    private long beta;
    private long depth;
    private long value;
    private Point bestMoveFrom;
    private Point bestMoveTo;
    private char promotion;

    public EvaluationRecord(long zobristHash, long alpha, long beta, long depth, long value, Point bestMoveFrom, Point bestMoveTo, char promotion) {
        this.zobristHash = zobristHash;
        this.alpha = alpha;
        this.beta = beta;
        this.depth = depth;
        this.value = value;
        this.bestMoveFrom = bestMoveFrom;
        this.bestMoveTo = bestMoveTo;
        this.promotion = promotion;
    }

    public EvaluationRecord(long zobristHash, long alpha, long beta, long depth, long value, Point bestMoveFrom, Point bestMoveTo)
    {
        this(zobristHash,alpha,beta,depth,value,bestMoveFrom,bestMoveTo,' ');
    }

    public long getZobristHash() {
        return zobristHash;
    }

    public long getAlpha() {
        return alpha;
    }

    public long getBeta() {
        return beta;
    }

    public long getDepth() {
        return depth;
    }

    public long getValue() {
        return value;
    }

    public Point getBestMoveFrom() {
        return bestMoveFrom;
    }

    public Point getBestMoveTo() {
        return bestMoveTo;
    }

    public char getPromotion() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationRecord that = (EvaluationRecord) o;
        return getZobristHash() == that.getZobristHash();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getZobristHash(), getAlpha(), getBeta(), getDepth(), getValue(), getBestMoveFrom(), getBestMoveTo(), getPromotion());
    }
}
