package utils;

import pieces.*;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ChessUtils {
    /**
     * Fonction de conversion d'un Point, en un String qui équivaux aux coodonnées d'une case.
     * @param position
     * @return Le numéro de la case.
     * @throws IllegalArgumentException
     */
    public static String pointToString(Point position) throws IllegalArgumentException
    {
        if(position.x > 7 || position.x < 0 || position.y > 7 || position.y < 0)
            throw new IllegalArgumentException("Position Out Of Bounds");

        return String.valueOf((char) ((int) 'a' + position.x)) + (position.y + 1);
    }
    public static Piece pieceFromChar(char pieceName, Point position, boolean isWhite) throws IllegalArgumentException
    {
        switch(pieceName)
        {
            case Bishop.SHORTNAME:
                return new Bishop(isWhite, position);
            case King.SHORTNAME:
                return new King(isWhite, position);
            case Knight.SHORTNAME:
                return new Knight(isWhite, position);
            case Pawn.SHORTNAME:
                return new Pawn(isWhite, position);
            case Queen.SHORTNAME:
                return new Queen(isWhite, position);
            case Rook.SHORTNAME:
                return new Rook(isWhite, position);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * toCoord - Fonction permettant de vérifier q'une case appartient au plateau à partir de ces coordonnées.
     * @param position La position de la case.
     * @return Retourne le point si elle existe sinon retourne null.
     */
    public static Point toCoord(Point position) {
        if (position != null) {
                    if (position.x < 0 || position.x > 7 || position.y < 0 || position.y > 7)
                        return null;
                    return position;
        }else
            return null;
    }

    /**
     * toCoord - Fonction permettant de vérifier q'une case appartient au plateau à partir du nom de la case.
     * @param position
     * @return
     */
    public static Point toCoord(String position){
        position = position.toLowerCase();
        if (position.length()== 2)
            if (position.charAt(0) >= 'a' && position.charAt(0) <= 'h' && position.charAt(1) >= 1 && position.charAt(1) <= 8)
                    return new Point((int)position.charAt(0) - (int)('a'), (int)position.charAt(1) - (int)'1');
        return null;
    }

    public static <T> boolean testForAll(T[] testArray, Function<T, Boolean> function)
    {
        for(T value: testArray)
            if(!function.apply(value))
                return false;
        return true;
    }

    public static String toStringPos(Point pos)
    {
        return (char)('A'+pos.x) + "" +(pos.y+1);
    }
}
