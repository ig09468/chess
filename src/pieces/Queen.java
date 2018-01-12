package pieces;

/** Classe pour les reines
 * @extends structures.Piece
 *
 * */
public class Queen extends Piece {

    public Queen(String color, int posX, int posY){
        super(color,posX,posY);
    }

    public static String toShortString() {
        return "Q";
    }
}
