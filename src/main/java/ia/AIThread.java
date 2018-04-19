package ia;

import javafx.application.Platform;
import layout.Controller;

import java.util.ArrayList;

public class AIThread extends Thread{
    public static ArrayList<Long> times = new ArrayList<>();

    private boolean isWhite;
    private boolean terminate;
    public AIThread(boolean isWhite)
    {
        this.isWhite = isWhite;
        terminate = false;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            AIMovement move = isWhite ? Controller.currentGame.getWhiteAI().getNextMove() : Controller.currentGame.getBlackAI().getNextMove();
            if(move != null && !terminate)
            {
                Platform.runLater(()->{
                    Controller.currentGame.getBoard().move(move.getFrom(), move.getTo(), true, false, move.getPromotion() != ' ' ? move.getPromotion(): null);
                    long end = System.currentTimeMillis();
                    times.add(end-start);
                    long meanTime = meanTime();
                    Controller.meanComputingLabel.setText((meanTime)/1000 + ","+String.format("%03d",(meanTime)%1000)+"s");
                    Controller.computingLabel.setText((isWhite ? "White" : "Black")+" move computed in " + (end-start)/1000 + ","+String.format("%03d",(end-start)%1000)+"s");
                    Controller.boardLock=false;
                    Controller.undobutton.setDisable(false);
                    if(Controller.currentGame.getBoard().calculateStatus())
                        Controller.doNextMove();
                });
            }
        } catch (InterruptedException e) {
            System.out.println((isWhite ? "White" : "Black")+" AI Interrupted");
            Controller.boardLock=false;
        }
    }

    public void terminate()
    {
        terminate=true;
        if(isWhite)
        {
            Controller.currentGame.getWhiteAI().terminate();
        }else
        {
            Controller.currentGame.getBlackAI().terminate();
        }
    }

    private long meanTime()
    {
        long moy=0;
        long count=0;
        for(Long time : times)
        {
            moy+=time;
            count++;
        }
        return count > 0 ? moy/count : 0;
    }
}
