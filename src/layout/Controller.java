package layout;

import logique.Game;

public class Controller {
    public static Game currentGame;

    public static void showGame()
    {
        //TODO Affiche le plateau de la partie en cours
    }

    public static void checkMate(boolean whiteWin)
    {
        if(whiteWin)
        {
            //TODO Affiche la victoire des blancs
        }else
        {
            //TODO Affiche la victoire des noirs
        }
    }

    public static void staleMate()
    {
        //TODO Affiche le pat (pas de vainqueur)
    }
}
