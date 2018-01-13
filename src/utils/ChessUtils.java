package utils;

import pieces.*;

import java.awt.*;

public class ChessUtils {
    public static String pointToString(Point p) throws IllegalArgumentException
    {
        if(p.x > 7 || p.x < 0 || p.y > 7 || p.y < 0)
            throw new IllegalArgumentException("Position Out Of Bounds");

        return String.valueOf((char) ((int) 'a' + p.x)) + (p.y + 1);
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
}
