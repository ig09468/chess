package pieces;

public class Pawn extends Pieces {

    private boolean LastMoveIsDouble;

    public Pawn(String color, int posX, int posY) {
        super(color, posX, posY);
        this.LastMoveIsDouble=false;
    }


    public static String toShortString(){
        return "P";
    }
}
