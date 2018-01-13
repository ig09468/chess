package logique;


import pieces.*;
import utils.ChessUtils;

import java.awt.*;
import java.util.ArrayList;

public class Board {

    //Cases sélectionnées
    private ArrayList<Tile> selectedTiles;

    //Pièce sélectionée
    private Piece selectedPiece;

    //Booléen désignant si le plateau a changé depuis la dernière évaluation de son état
    private boolean changed;

    //Etat du plateau
    private String etat;

    //Booléen désignant le plateau comme étant en buffer ou non
    private boolean isBuffer;

    //Booléen désignant si c'est le tour des blancs
    private boolean isWhiteTurn;

    //Historique des mouvements
    private ArrayList<Mouvement> moveHistory;

    public Board()
    {

    }

    public Tile getTile(Point position){

    }
}
