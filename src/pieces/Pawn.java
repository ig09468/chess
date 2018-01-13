package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {

    public static final char SHORTNAME = 'P';

    /* Stock si le dernier mouvement est double. */
    private boolean LastMoveIsDouble;

    /* Tableau de prise passants position */
    protected ArrayList <Point> enPassantCapturePos;


    /**
     * Constructor de la classe.
     * @param white La couleur de la piece
     * @param position Position de la piece
     */

    public Pawn(boolean white, Point position) {
        super(white, position);
        this.LastMoveIsDouble=false;
    }

    /**
     * Donne le si le dernier mouvement est double.
     * @return LastMoveIsDouble
     */
    public boolean getLastMoveIsDouble(){
        return LastMoveIsDouble;
    }


    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

        /* Vérification de boardInstance */
        if(boardInstance!=null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }
        /* Vidage du tableau des mouvements légaux */
        legalMoves.clear();


        int posY = (this.white ? 1 : -1);
        /* Affectation du point de test, déplacement en diagonale */
        Point testPos = new Point(this.position.x, this.position.y + posY);

        /* Case à tester */
        Tile testTile = boardInstance.getTile(testPos);

        /*Vérification de la case devant le pion */
        if(testTile!=null && !testTile.isOccupied()) {
            this.legalMoves.add(testPos);

            /* Nouvelle attribution de valeur en y */
            testPos.setLocation(testPos.x,testPos.y + posY);
            testTile=boardInstance.getTile(testPos);

            /* Test si la deuxième case est disponible */
            if(this.hasNeverMoved && testTile!=null && !testTile.isOccupied()){
                this.legalMoves.add(testPos);
            }
        }

        /* Affectation de la nouvelle position */
        testPos.setLocation(this.position.x + 1, this.position.y + ((this.white) ? 1 : -1) );
        testTile=boardInstance.getTile(testPos);

        /* Vérification si testTile existe bien */
        if (testTile!=null){
            if(testTile.isOccupied()){

                /* Vérifie que la pièce en diagonale est bien d'une couleur différente,
                   si c'est bon, on l'ajoute à la liste */
                if(testTile.getPiece().isWhite()!=this.white){
                    this.legalMoves.add(testPos);
                }else
                {
                    /* Nouveau point à test, pour savoir l'une des pièces peuvent être prise enPassant */
                    Point testPos2 = new Point(this.position.x-1, this.position.y);
                    testTile = boardInstance.getTile(testPos2);
                    /* Recherche de la prise en passant */
                    if(testTile.getPiece() instanceof Pawn){
                        Pawn testTilePiece= (Pawn)testTile.getPiece();
                        if(testTile.isOccupied() && testTilePiece.LastMoveIsDouble)
                        {
                            /* Enregistre les coordonnées de la pièce mangée */
                            this.enPassantCapturePos.add(testPos2);
                            /* Enregistre le mouvement légal */
                            this.legalMoves.add(testPos);
                        }
                    }
                }
            }
            this.decimateLegalMovesCheck(boardInstance);
            this.legalMovesCalculated = true;
        }






    }

    /**
     * toShortString - Donne la représentation officielle de la pièce
     * @return Représentation de la pièce
     */
    public static String toShortString(){
        return "P";
    }

}
