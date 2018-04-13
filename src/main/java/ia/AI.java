package ia;

import javafx.application.Platform;
import layout.Controller;
import logique.Board;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int level;
    public static final int[] depthArray = {0,2,3,4};
    private boolean whiteSide;
    private Board board;
    private AIMovement latestMovement;
    private static ZobristHash zobrist=null;
    public AI(boolean isWhiteSide,int level, Board board)
    {
        this.whiteSide = isWhiteSide;
        this.level = level;
        this.board = board;
        if(zobrist == null)
            zobrist = new ZobristHash();
    }

    public void autoplay() throws Exception {
        if(zobrist == null)
            zobrist = new ZobristHash();
        while(board.calculateStatus())
        {
            ArrayList<AIMovement> moves = board.getAvailableMoves();
            for(AIMovement move : moves)
            {
                latestMovement = move;
                board.move(move.getFrom(), move.getTo(), true, true);
                Platform.runLater(Controller::showGame);
                board.fullUndo();
            }
            Random rand = new Random();
            AIMovement move = moves.get(rand.nextInt(moves.size()));
            latestMovement = move;
            board.move(move.getFrom(), move.getTo(), true, true);
            Platform.runLater(Controller::showGame);
            Thread.sleep(1000);
        }
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
            try {
                long bestValue = whiteSide ? Long.MIN_VALUE : Long.MAX_VALUE;
                for (AIMovement move : moves) {
                    latestMovement = move.clone();
                    if(!board.move(move.getFrom(), move.getTo(), true, true))
                        throw new Exception("Invalid move : " + move);

                    board.resetCalculatedLegalMoves();
                    long nextValue = minimax(depthArray[level > 3 ? 3 : level] - 1, !whiteSide);
                    board.fullUndo();

                    if ((whiteSide && nextValue >= bestValue) || (!whiteSide && nextValue <= bestValue)) {
                        bestMove = move.clone();
                        bestValue = nextValue;
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                Controller.showGame();
            }
            return bestMove;
        }
        return null;
    }

    private long minimax(int depth, boolean isMax) throws Exception {
        ArrayList<AIMovement> moves = board.getAvailableMoves();
        if(moves.isEmpty())
        {
            return isMax ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
        if(depth==0)
        {
            return board.getValue();
        }
        long bestValue = isMax ? Long.MIN_VALUE : Long.MAX_VALUE;
        for (AIMovement move : moves) {
            AIMovement previousMovement = latestMovement.clone();
            latestMovement = move.clone();

            if(!board.move(move.getFrom(), move.getTo(), true, true))
                throw new Exception("Invalid move : " + move);

            long nextValue = minimax(depth - 1, !isMax);

            board.fullUndo();
            latestMovement = previousMovement.clone();
            if ((isMax && bestValue <= nextValue) || (!isMax && bestValue >= nextValue)) {
                bestValue = nextValue;
            }
        }
        return bestValue;
    }
}
