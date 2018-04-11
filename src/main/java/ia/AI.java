package ia;

import layout.Controller;
import logique.Board;
import logique.MoveRecord;
import pieces.Piece;
import utils.ChessUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class AI {
    private int historyLength;
    private int level;
    public static final int[] depthArray = {0,2,3,4};
    private boolean whiteSide;
    private Board board;
    private AIMovement latestMovement;
    public AI(boolean isWhiteSide,int level, Board board)
    {
        this.whiteSide = isWhiteSide;
        this.level = level;
        this.board = board;
    }

    public char choosePromotion()
    {
        char promotion=' ';
        if(latestMovement != null)
            promotion = latestMovement.getPromotion();
        return board.promote(promotion);
    }

    public AIMovement getNextMove()
    {
        if(level < 1)
        {
            ArrayList<AIMovement> moves =  board.getAvailableMoves();
            if(!moves.isEmpty())
            {
                Random rand = new Random();
                return moves.get(rand.nextInt(moves.size()));
            }
        }else
        {
            historyLength = board.moveHistory.size();
            return minimaxRoot(depthArray[level > 3 ? 3 : level]);
        }
        return null;
    }

    private AIMovement minimaxRoot(int depth)
    {
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        AIMovement bestMove=null;
        if(!moves.isEmpty())
        {
            long alpha=Long.MIN_VALUE, beta=Long.MAX_VALUE, mAlpha=alpha, mBeta=beta;
            moves.sort(Comparator.comparingLong(AIMovement::getKillerMoveValue));
            bestMove = moves.get(0);
            AIMovement previousMove;
            if(whiteSide)
            {
                long bestValue = Long.MIN_VALUE;
                for (AIMovement move : moves) {
                    previousMove = latestMovement;
                    latestMovement = move;
                    board.move(move.getFrom(), move.getTo(), true, true);
                    historyLength++;
                    if(historyLength != board.moveHistory.size())
                        System.err.println("Wrong");
                    System.out.println("Moved : " + ChessUtils.toStringPos(board.getLastMove().getOldPos()) + "->" + ChessUtils.toStringPos(board.getLastMove().getNewPos()));
                    board.resetCalculatedLegalMoves();
                    
                    long nextDepth = minimax(depth - 1, mAlpha, mBeta, false);
                    System.out.println("Undo");
                    board.undo();
                    board.resetCalculatedLegalMoves();
                    historyLength--;
                    if(historyLength != board.moveHistory.size())
                        System.err.println("Wrong");
                    latestMovement=previousMove;
                    if (nextDepth > bestValue) {
                        bestMove = move;
                        bestValue = nextDepth;
                    }
                    /*if (mAlpha < bestValue) {
                        mAlpha = bestValue;
                    }*/
                }
            }else
            {
                long bestValue = Long.MAX_VALUE;
                for (AIMovement move : moves) {
                    previousMove = latestMovement;
                    latestMovement = move;
                    board.move(move.getFrom(), move.getTo(), true, true);
                    historyLength++;
                    if(historyLength != board.moveHistory.size())
                        System.err.println("Wrong");
                    System.out.println("Moved : " + ChessUtils.toStringPos(board.getLastMove().getOldPos()) + "->" + ChessUtils.toStringPos(board.getLastMove().getNewPos()));
                    board.resetCalculatedLegalMoves();
                    
                    long nextDepth = minimax(depth - 1, mAlpha, mBeta, true);
                    System.out.println("Undo");
                    board.undo();
                    board.resetCalculatedLegalMoves();
                    historyLength--;
                    if(historyLength != board.moveHistory.size())
                        System.err.println("Wrong");
                    latestMovement = previousMove;
                    if (nextDepth < bestValue) {
                        bestMove = move;
                        bestValue = nextDepth;
                    }
                    /*if (mBeta > bestValue) {
                        mBeta = bestValue;
                    }*/
                }
            }
        }
        return bestMove;
    }

    private long minimax(int depth, long alpha, long beta, boolean isMax)
    {
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        if(moves.isEmpty())
        {
            if(board.isWhiteTurn())
            {
                return board.getKing(true).isAttacked(board) ? Long.MIN_VALUE : Long.MIN_VALUE+10;
            }else
            {
                return board.getKing(false).isAttacked(board) ? Long.MAX_VALUE : Long.MAX_VALUE-10;
            }
        }
        if(depth == 0)
            return board.getValue();
        moves.sort(Comparator.comparingLong(AIMovement::getKillerMoveValue));
        long mAlpha = alpha, mBeta = beta;
        AIMovement previousMove;
        if(isMax)
        {
            long bestValue = Long.MIN_VALUE;
            for (AIMovement move : moves) {
                previousMove = latestMovement;
                latestMovement = move;
                board.move(move.getFrom(), move.getTo(), true, true);
                historyLength++;
                if(historyLength != board.moveHistory.size())
                    System.err.println("Wrong");
                System.out.println("Moved : " + ChessUtils.toStringPos(board.getLastMove().getOldPos()) + "->" + ChessUtils.toStringPos(board.getLastMove().getNewPos()));
                board.resetCalculatedLegalMoves();
                
                long nextDepth = minimax(depth - 1, mAlpha, mBeta, false);
                System.out.println("Undo");
                board.undo();
                board.resetCalculatedLegalMoves();
                historyLength--;
                if(historyLength != board.moveHistory.size())
                    System.err.println("Wrong");
                latestMovement=previousMove;
                if (nextDepth > bestValue) {
                    bestValue = nextDepth;
                }
                /*if (bestValue >= mBeta) {
                    return bestValue;
                }
                if (mAlpha < bestValue) {
                    mAlpha = bestValue;
                }*/
            }
            return bestValue;
        }else
        {
            long bestValue = Long.MAX_VALUE;
            for (AIMovement move : moves) {
                previousMove = latestMovement;
                latestMovement = move;
                board.move(move.getFrom(), move.getTo(), true, true);
                historyLength++;
                if(historyLength != board.moveHistory.size())
                    System.err.println("Wrong");
                System.out.println("Moved : " + ChessUtils.toStringPos(board.getLastMove().getOldPos()) + "->" + ChessUtils.toStringPos(board.getLastMove().getNewPos()));
                board.resetCalculatedLegalMoves();
                
                long nextDepth = minimax(depth - 1, mAlpha, mBeta, true);
                System.out.println("Undo");
                board.undo();
                board.resetCalculatedLegalMoves();
                historyLength--;
                if(historyLength != board.moveHistory.size())
                    System.err.println("Wrong");
                latestMovement = previousMove;
                if (nextDepth < bestValue) {
                    bestValue = nextDepth;
                }
                /*if (bestValue >= mAlpha) {
                    return bestValue;
                }
                if (mBeta < bestValue) {
                    mBeta = bestValue;
                }*/
            }
            return bestValue;
        }
    }


}
