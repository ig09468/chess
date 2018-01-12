package pieces;

/** Classe pour les fous
 * @extends Pieces
 *
 * */

public class Bishop extends Pieces {

    public Bishop(String color, int posX, int posY){
        super(color,posX,posY);
    }

    public static String toShortString(){
        return "B";
    }
}
