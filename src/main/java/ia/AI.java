package ia;

import logique.Board;

public class AI {

    private int level;
    private boolean whiteSide;
    private Board board;
    public AI(boolean isWhiteSide,int level, Board board)
    {
        this.whiteSide = isWhiteSide;
        this.level = level;
        this.board = board;
    }

    public char choosePromotion()
    {
        char promotion=' ';
        //TODO Choisit la promotion du pion
        return board.promote(promotion);
    }
}
