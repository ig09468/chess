package pieces;

/** Classe pour les fous
 * @extends Piece
 *
 * */

public class Bishop extends Piece {

    public Bishop(String color, int posX, int posY){
        super(color,posX,posY);
    }

    public static String toShortString(){
        return "B";
    }
}
