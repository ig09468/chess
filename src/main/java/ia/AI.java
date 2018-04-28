package ia;

import layout.Controller;
import logique.Board;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int level;
    private boolean whiteSide;
    private Board board;
    private boolean terminate;
    public AI(boolean isWhiteSide,int level, Board board)
    {
        this.whiteSide = isWhiteSide;
        this.level = level;
        this.board = board;
        terminate = false;
    }

    public AIMovement getNextMove() throws InterruptedException
    {
        Random rand = new Random();
        ArrayList<AIMovement> moves =  board.getAvailableMoves();
        if(level < 1)
        {
            if(!moves.isEmpty())
            {
                return moves.get(rand.nextInt(moves.size()));
            }
        }else
        {
            Controller.zobrist.reset();
            ArrayList<AIMovement> bestMove = new ArrayList<>();
            long bestValue = whiteSide ? Long.MIN_VALUE : Long.MAX_VALUE;
            long alpha = Long.MIN_VALUE, savedAlpha = Long.MIN_VALUE;
            long beta = Long.MAX_VALUE, savedBeta = Long.MAX_VALUE;
            EvaluationRecord ev = Controller.zobrist.get(board.getHash());
            if(ev == null || ev.shouldReevaluate(level, alpha, beta))
            {
                moves.sort((a,b)-> Long.compare(b.getKillerMoveValue(), a.getKillerMoveValue()));
                for (AIMovement move : moves) {
                    if(terminate)
                        throw new InterruptedException();

                    board.move(move.getFrom(), move.getTo(), true, true, move.getPromotion() != ' ' ? move.getPromotion() : null);
                    board.resetCalculatedLegalMoves();
                    long nextValue = minimax(level - 1, alpha, beta,!whiteSide);
                    board.fullUndo();
                    if(nextValue == bestValue)
                    {
                        bestMove.add(move.clone());
                    }
                    if ((whiteSide && nextValue > bestValue) || (!whiteSide && nextValue < bestValue)) {
                        bestMove.clear();
                        bestMove.add(move.clone());
                        bestValue = nextValue;
                    }
                    if(whiteSide && bestValue >= beta || (!whiteSide && bestValue<=alpha)) {
                        break;
                    }
                    if(whiteSide)
                    {
                        if(bestValue > alpha)
                            alpha = bestValue;
                    }else
                    {
                        if(bestValue <beta)
                            beta = bestValue;
                    }
                }
            }else
            {
                bestMove.clear();
                bestMove.add(ev.getBestMove());
            }

            if(terminate)
                throw new InterruptedException();
            if(bestMove.isEmpty()) {
                return null;
            }
            if(bestMove.size() == 1) {
                AIMovement move = bestMove.get(0);
                saveMoveToTT(bestValue, savedAlpha, savedBeta, move, whiteSide);
                return bestMove.get(0);
            }
            AIMovement move = bestMove.get(rand.nextInt(bestMove.size()));
            saveMoveToTT(bestValue, savedAlpha, savedBeta, move, whiteSide);
            return move;
        }
        if(terminate)
            throw new InterruptedException();
        return null;
    }

    private void saveMoveToTT(long bestValue, long alpha, long beta, AIMovement move, boolean isMax) {
        if(bestValue <= alpha)
        {
            Controller.zobrist.put(board.getHash(), new EvaluationRecord(board.getHash(), alpha, beta, level, bestValue, move, EvaluationRecord.ValueType.ALPHACUT));
        }else if(bestValue >= beta)
        {
            Controller.zobrist.put(board.getHash(), new EvaluationRecord(board.getHash(), alpha, beta, level, bestValue, move, EvaluationRecord.ValueType.BETACUT));
        }else
        {
            Controller.zobrist.put(board.getHash(), new EvaluationRecord(board.getHash(), alpha, beta, level, bestValue, move, EvaluationRecord.ValueType.NORMAL));
        }
    }

    private long minimax(int depth, long alpha, long beta, boolean isMax) throws InterruptedException {
        if(terminate)
            throw new InterruptedException();
        EvaluationRecord ev = Controller.zobrist.get(board.getHash());
        if(ev != null && !ev.shouldReevaluate(depth, alpha, beta)){
            return ev.getValue();
        }
        if(!board.calculateStatus(false))
        {
            long value;
            if(isMax)
            {
                value = board.getKing(true).isAttacked(board) ? Long.MIN_VALUE : Long.MIN_VALUE + 10;
                saveMoveToTT(value, alpha, beta, null, isMax);
                return value;
            }else
            {
                value = board.getKing(false).isAttacked(board) ? Long.MAX_VALUE : Long.MAX_VALUE - 10;
                saveMoveToTT(value, alpha, beta, null, isMax);
                return value;
            }
        }
        if(depth==0)
        {
            long value = board.getValue();
            saveMoveToTT(value, alpha, beta, null, isMax);
            return value;
        }
        long savedAlpha = alpha, savedBeta = beta;
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        moves.sort((a,b)-> Long.compare(b.getKillerMoveValue(), a.getKillerMoveValue()));

        if(ev != null && ev.getBestMove() != null)
        {
            int in = moves.indexOf(ev.getBestMove());
            if(in != -1)
            {
                moves.add(0,moves.remove(in));
            }
        }
        long bestValue = isMax ? Long.MIN_VALUE : Long.MAX_VALUE;
        AIMovement bestMove = moves.isEmpty() ? null : moves.get(0);
        for (AIMovement move : moves) {
            if(terminate)
                throw new InterruptedException();
            board.move(move.getFrom(), move.getTo(), true, true, move.getPromotion() != ' ' ? move.getPromotion() : null);
            long nextValue = minimax(depth - 1,alpha,beta, !isMax);

            board.fullUndo();
            if ((isMax && bestValue <= nextValue) || (!isMax && bestValue >= nextValue)) {
                bestValue = nextValue;
                //bestMove = move;
            }
            if(isMax)
            {
                if(bestValue > alpha)
                {
                    alpha = bestValue;
                }

            }else
            {
                if(bestValue < beta)
                    beta = bestValue;
            }
            if(beta <= alpha) {
                break;
            }

        }
        saveMoveToTT(bestValue, savedAlpha, savedBeta, bestMove,isMax);
        return bestValue;
    }

    public void terminate() {
        terminate=true;
    }
}
