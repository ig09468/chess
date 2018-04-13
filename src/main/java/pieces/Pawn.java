package pieces;

import logique.Board;
import logique.Tile;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {

    public static final char SHORTNAME = 'P';

    /* Tableau de prise passants position */
    private ArrayList <Point> enPassantCapturePos;


    /**
     * Constructor de la classe.
     * @param white La couleur de la piece
     * @param position Position de la piece
     */

    public Pawn(boolean white, Point position) {
        super(white, position);
        enPassantCapturePos = new ArrayList<>();
    }


    /**
     * Calcule du mouvement d'une piece.
     * @param boardInstance
     */
    public void calculateLegalMoves(Board boardInstance){

        /* Vérification de boardInstance */
        if(boardInstance==null) {
            System.out.print("console.error : Board undefined for calculateLegalMoves()\n");
            return;
        }
        /* Vidage du tableau des mouvements légaux */
        legalMoves.clear();


        int posY = (isWhite() ? 1 : -1);
        /* Affectation du point de test, déplacement en diagonale */
        Point testPos = new Point(this.position.x, this.position.y + posY);

        /* Case à tester */
        Tile testTile = boardInstance.getTile(testPos);

        /*Vérification de la case devant le pion */
        if(testTile!=null && !testTile.isOccupied()) {
            this.legalMoves.add((Point)testPos.clone());

            /* Nouvelle attribution de valeur en y */
            testPos.setLocation(testPos.x,testPos.y + posY);
            testTile=boardInstance.getTile(testPos);

            /* Test si la deuxième case est disponible */
            if(this.position.y == (this.white ? 1 : 6) && testTile!=null && !testTile.isOccupied()){
                this.legalMoves.add((Point)testPos.clone());
            }
        }

        /* Affectation de la nouvelle position */
        testPos = new Point(this.position.x+1, this.position.y + posY);
        testTile=boardInstance.getTile((Point)testPos.clone());

        /* Vérification si testTile existe bien */
        if (testTile!=null){
            if(testTile.isOccupied()){

                /* Vérifie que la pièce en diagonale est bien d'une couleur différente,
                   si c'est bon, on l'ajoute à la liste */
                if(isWhite() != testTile.getPiece().isWhite()){
                    this.legalMoves.add((Point)testPos.clone());
                }
            }else
            {
                /* Nouveau point à test, pour savoir l'une des pièces peuvent être prise enPassant */
                Point testPos2 = new Point(this.position.x+1, this.position.y);
                testTile = boardInstance.getTile(testPos2);
                /* Recherche de la prise en passant */
                addEnPassantLegalMove(boardInstance, testPos, testTile, testPos2);
            }
        }
        /* Affectation de la nouvelle position */
        testPos.setLocation(this.position.x-1, this.position.y + posY );
        testTile=boardInstance.getTile((Point)testPos.clone());

        /* Vérification si testTile existe bien */
        if (testTile!=null){
            if(testTile.isOccupied()){

                /* Vérifie que la pièce en diagonale est bien d'une couleur différente,
                   si c'est bon, on l'ajoute à la liste */
                if(testTile.getPiece().isDiffColor(isWhite())){
                    this.legalMoves.add((Point)testPos.clone());
                }
            }else
            {
                /* Nouveau point à test, pour savoir l'une des pièces peuvent être prise enPassant */
                Point testPos2 = new Point(this.position.x-1, this.position.y);
                testTile = boardInstance.getTile(testPos2);
                /* Recherche de la prise en passant */
                addEnPassantLegalMove(boardInstance, testPos, testTile, testPos2);
            }
        }

        this.legalMovesCalculated = true;
        this.decimateLegalMovesCheck(boardInstance);
    }

    private void addEnPassantLegalMove(Board boardInstance, Point testPos, Tile testTile, Point testPos2) {
        if(testTile.getPiece() instanceof Pawn){
            Pawn testTilePiece= (Pawn)testTile.getPiece();
            if(testTile.isOccupied() && boardInstance.compareToEnPassantCandidat(testTilePiece))
            {
                /* Enregistre les coordonnées de la pièce mangée */
                this.enPassantCapturePos.add((Point)testPos2.clone());
                /* Enregistre le mouvement légal */
                this.legalMoves.add((Point)testPos.clone());
            }
        }
    }

    /**
     * toShortString - Donne la représentation officielle de la pièce
     * @return Représentation de la pièce
     */
    public static String toShortString(){
        return "P";
    }
    public char toShortName(){ return SHORTNAME; }

    public Point getCapturePos(int xCoord)
    {
        for(Point p : enPassantCapturePos)
        {
            if(p.x == xCoord)
                return p;
        }
        return null;
    }

    @Override
    public void resetLegalMoves()
    {
        super.resetLegalMoves();
        this.enPassantCapturePos.clear();
    }

}
