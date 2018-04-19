package ia;

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
            ArrayList<AIMovement> bestMove = new ArrayList<>();
            long bestValue = whiteSide ? Long.MIN_VALUE : Long.MAX_VALUE;

            for (AIMovement move : moves) {
                if(terminate)
                    throw new InterruptedException();
                board.move(move.getFrom(), move.getTo(), true, true, move.getPromotion() != ' ' ? move.getPromotion() : null);
                board.resetCalculatedLegalMoves();
                long nextValue = minimax(level - 1, !whiteSide);
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
            }
            if(terminate)
                throw new InterruptedException();
            if(bestMove.isEmpty())
                return null;
            if(bestMove.size() == 1)
                return bestMove.get(0);
            return bestMove.get(rand.nextInt(bestMove.size()));
        }
        if(terminate)
            throw new InterruptedException();
        return null;
    }

    private long minimax(int depth, boolean isMax) throws InterruptedException {
        if(terminate)
            throw new InterruptedException();
        if(!board.calculateStatus(false))
        {
            if(isMax)
            {
                return board.getKing(true).isAttacked(board) ? Long.MIN_VALUE : Long.MIN_VALUE + 10;
            }else
            {
                return board.getKing(false).isAttacked(board) ? Long.MAX_VALUE : Long.MAX_VALUE - 10;
            }
        }
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        if(depth==0)
        {
            return board.getValue();
        }
        long bestValue = isMax ? Long.MIN_VALUE : Long.MAX_VALUE;
        for (AIMovement move : moves) {
            if(terminate)
                throw new InterruptedException();
            board.move(move.getFrom(), move.getTo(), true, true, move.getPromotion() != ' ' ? move.getPromotion() : null);
            long nextValue = minimax(depth - 1, !isMax);

            board.fullUndo();
            if ((isMax && bestValue <= nextValue) || (!isMax && bestValue >= nextValue)) {
                bestValue = nextValue;
            }
        }
        return bestValue;
    }

    public void terminate() {
        terminate=true;
    }
}
