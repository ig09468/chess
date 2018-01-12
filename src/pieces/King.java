package pieces;

/** Classe pour les rois
 * @extends Piece
 *
 * */
public class King extends Pieces {

    public King(String color, int posX, int posY){
        super(color,posX,posY);
    }

    public static String toShortString(){
        return "K";
    }
}
