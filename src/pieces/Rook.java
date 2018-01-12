package pieces;

public class Rook extends Pieces {

    public Rook(String color, int posX, int posY) {
        super(color, posX, posY);
    }

    public static String toShortString(){
        return "R";
    }
}
