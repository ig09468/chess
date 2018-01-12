package pieces;

/** Classe pour les cavaliers
 * @extends Piece
 *
 * */
public class Knight extends Pieces {

    public Knight(String color, int posX, int posY){
        super(color,posX,posY);
    }

    public static String toShortString(){
        return "K";
    }
}
