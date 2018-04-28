package ia;

import java.util.Objects;

public class EvaluationRecord {
    public AIMovement getBestMove() {
        return bestMove;
    }

    public enum ValueType {ALPHACUT, BETACUT, NORMAL}
    private long zobristHash;
    private long alpha;
    private long beta;
    private long depth;
    private long value;
    private ValueType type;
    private AIMovement bestMove;

    public EvaluationRecord(long zobristHash, long alpha, long beta, long depth, long value, AIMovement bestMove, ValueType type) {
        this.zobristHash = zobristHash;
        this.alpha = alpha;
        this.beta = beta;
        this.depth = depth;
        this.value = value;
        this.bestMove = bestMove;
        this.type = type;
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

    public ValueType getType(){return type;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationRecord that = (EvaluationRecord) o;
        return getZobristHash() == that.getZobristHash();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getZobristHash(), getAlpha(), getBeta(), getDepth(), getValue(), getBestMove(), getType());
    }

    public boolean shouldReevaluate(long depth, long alpha, long beta)
    {
        if(depth > this.depth)
            return true;
        if(type == ValueType.NORMAL)
            return false;
        if(type == ValueType.ALPHACUT)
            return alpha < this.alpha;
        return beta > this.beta;
    }
}
