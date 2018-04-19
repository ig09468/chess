package ia;

import logique.Board;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int level;
    private boolean whiteSide;
    private Board board;
    private AIMovement latestMovement;
    private boolean terminate;
    public AI(boolean isWhiteSide,int level, Board board)
    {
        this.whiteSide = isWhiteSide;
        this.level = level;
        this.board = board;
        terminate = false;
    }

    public char choosePromotion()
    {
        char promotion=' ';
        if(latestMovement != null)
            promotion = latestMovement.getPromotion();
        return board.promote(promotion);
    }

    public AIMovement getNextMove() throws InterruptedException
    {
        ArrayList<AIMovement> moves =  board.getAvailableMoves();
        if(level < 1)
        {
            if(!moves.isEmpty())
            {
                Random rand = new Random();
                return moves.get(rand.nextInt(moves.size()));
            }
        }else
        {
            AIMovement bestMove = null;
            long bestValue = whiteSide ? Long.MIN_VALUE : Long.MAX_VALUE;

            for (AIMovement move : moves) {
                if(terminate)
                    throw new InterruptedException();
                latestMovement = move.clone();
                board.move(move.getFrom(), move.getTo(), true, true);
                board.resetCalculatedLegalMoves();
                long nextValue = minimax(level - 1, !whiteSide);
                board.fullUndo();
                if ((whiteSide && nextValue >= bestValue) || (!whiteSide && nextValue <= bestValue)) {
                    bestMove = move.clone();
                    bestValue = nextValue;
                }
            }
            if(terminate)
                throw new InterruptedException();
            return bestMove;
        }
        if(terminate)
            throw new InterruptedException();
        return null;
    }

    private long minimax(int depth, boolean isMax) throws InterruptedException {
        if(terminate)
            throw new InterruptedException();
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        if(moves.isEmpty())
        {
            if(isMax)
            {
                return board.getKing(true).isAttacked(board) ? Long.MIN_VALUE : Long.MIN_VALUE + 10;
            }else
            {
                return board.getKing(false).isAttacked(board) ? Long.MAX_VALUE : Long.MAX_VALUE - 10;
            }
        }
        if(depth==0)
        {
            return board.getValue();
        }
        long bestValue = isMax ? Long.MIN_VALUE : Long.MAX_VALUE;
        for (AIMovement move : moves) {
            if(terminate)
                throw new InterruptedException();
            AIMovement previousMovement = latestMovement.clone();
            latestMovement = move.clone();
            board.move(move.getFrom(), move.getTo(), true, true);
            long nextValue = minimax(depth - 1, !isMax);

            board.fullUndo();
            latestMovement = previousMovement.clone();
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
