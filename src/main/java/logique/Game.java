package logique;

import ia.AI;
import layout.PromotionStage;

public class Game {
    private Board board;
    private int whiteAILevel;
    private int blackAILevel;
    private int delay;
    private AI whiteAI;
    private AI blackAI;
    public Game(int whiteAILevel, int blackAILevel, int delay)
    {
        board = new Board();
        this.whiteAILevel = whiteAILevel;
        this.blackAILevel = blackAILevel;
        this.delay = delay;
        whiteAI =  new AI(true, whiteAILevel, board);
        blackAI =  new AI(false, blackAILevel, board);
    }

    public char askPromotion()
    {
        if(board != null && board.promotionNeeded())
        {
            if(board.isWhiteTurn() && whiteAI != null)
                return whiteAI.choosePromotion();
            else if(!board.isWhiteTurn() && blackAI !=null)
                return blackAI.choosePromotion();
            else
                return showPromotion();
        }
        return ' ';
    }

    public char showPromotion()
    {
        char promotion;

        PromotionStage pStage = new PromotionStage(board.isWhiteTurn());
        promotion = pStage.promotion;

        return board.promote(promotion);
    }

    public Board getBoard() {
        return board;
    }

    public int getWhiteAILevel() {
        return whiteAILevel;
    }

    public int getBlackAILevel() {
        return blackAILevel;
    }

    public int getDelay() {
        return delay;
    }

    public AI getWhiteAI() {
        return whiteAI;
    }

    public AI getBlackAI() {
        return blackAI;
    }
}
