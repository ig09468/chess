package logique;

public class Game {
    private Board board;
    private int whiteAILevel;
    private int blackAILevel;
    private int delay;
    public Game(int whiteAILevel, int blackAILevel, int delay)
    {
        board = new Board();
        this.whiteAILevel = whiteAILevel;
        this.blackAILevel = blackAILevel;
        this.delay = delay;
    }
}
