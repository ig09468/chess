package ia;

import logique.Board;
import logique.MoveRecord;
import logique.Tile;
import pieces.Pawn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ZobristHash {
    private long[] zobristTable;
    public static final long ZOBRIST_VALUE_MAX=288230376151711743L;
    private static final int P20 = 1048576;
    private static final int P25 = 32*P20;
    private static final int P30 = 32*P25;
    private ArrayList<ArrayList<EvaluationRecord>> zobristMap;
    private long whiteTurnValue;
    private long blackTurnValue;

    public enum TableSize{P20, P25, P30}
    public static final TableSize DEFAULT_TABLE_SIZE = TableSize.P25;
    private TableSize chosenSize;

    private static final int pType[] = {1,0,0,0,0,0,0,0,0,2,0,0,3,0,4,5,6,7,8,9};
    //                                  B                 K     N   P Q R S T U
    // S = Pawn enPassantCandidat
    // T = King hasNeverMoved
    // U = Rook HasNeverMoved

    public ZobristHash(TableSize tableSize, long seed)
    {
        generateZobristTable(seed);
        int size = tableSize== TableSize.P20 ? P20 : tableSize==TableSize.P25? P25: P30;
        zobristMap = new ArrayList<>(size);
        for(int i=0; i<size; i++)
        {
            zobristMap.add(null);
        }
        chosenSize = tableSize;
    }

    public ZobristHash(long seed)
    {
        this(DEFAULT_TABLE_SIZE, seed);
    }
    public ZobristHash(TableSize tableSize)
    {
        generateZobristTable(null);
        int size = tableSize== TableSize.P20 ? P20 : tableSize==TableSize.P25? P25: P30;
        zobristMap = new ArrayList<>(size);
        for(int i=0; i<size; i++)
        {
            zobristMap.add(null);
        }
        chosenSize = tableSize;
    }

    public ZobristHash()
    {
        this(DEFAULT_TABLE_SIZE);
    }

    private void generateZobristTable(Long seed)
    {
        Random rand = seed == null ? new Random() : new Random(seed);
        zobristTable = new long[1216];
        for(int i=0; i<zobristTable.length; i++)
        {
            zobristTable[i]=(rand.nextLong()%ZOBRIST_VALUE_MAX)+1;
        }
        whiteTurnValue = (rand.nextLong()%ZOBRIST_VALUE_MAX)+1;
        blackTurnValue = (rand.nextLong()%ZOBRIST_VALUE_MAX)+1;
    }

    private long getZobristValueFor(Point pos)
    {
        return pos.x >=0 && pos.x <8 && pos.y >=0 && pos.y<8 ? zobristTable[8*pos.x + 13*pos.y] : 0;
    }

    private long getZobristValueFor(Tile tile, Board board)
    {
        if(tile != null)
        {
            if(tile.isOccupied())
            {
                char shortName = tile.getPiece().toShortName();
                if(shortName == 'P' && board.compareToEnPassantCandidat((Pawn)tile.getPiece()))
                {
                    shortName='S';
                }else if(shortName == 'K' && tile.getPiece().getHasNeverMoved())
                {
                    shortName='T';
                }else if(shortName == 'R' && tile.getPiece().getHasNeverMoved())
                {
                    shortName='U';
                }
                return getZobristValueFor(tile.getPosition(), shortName, tile.getPiece().isWhite()) + (tile.getPiece().getHasNeverMoved() ? 1:0);
            }else
            {
                return zobristTable[8*tile.getPosition().x + 13*tile.getPosition().y];
            }
        }
        return 0;
    }

    private long getZobristValueFor(Point pos, char pieceName, boolean isWhite)
    {
        if(pos.x >=0 && pos.x <8 && pos.y >=0 && pos.y<8)
        {
            int index = pieceName - 'B';
            if(index < 0 || index >= pType.length)
                index = 0;
            else {
                index = pType[index];
                if(index != 0)
                {
                    if (isWhite) {
                        index++;
                    } else {
                        index+=10;
                    }
                }
            }
            return zobristTable[8*pos.x + 13*pos.y + index];
        }
        return 0;
    }

    public long calculateZobristHash(Board board)
    {
        long hash = 0;
        for(int x=0; x<8;x++)
        {
            for(int y=0; y<8;y++)
            {
                hash+=getZobristValueFor(board.getTile(new Point(x,y)), board);
            }
        }
        return hash;
    }

    public long getHashChange(MoveRecord rec, char pieceType, boolean isWhite, Point newEnPassantCandidate)
    {
        long change=0;
        if(isWhite)
        {
            change+=whiteTurnValue;
            change-=blackTurnValue;
        }else
        {
            change-=whiteTurnValue;
            change+=blackTurnValue;
        }
        if(rec.isBigCastle())
        {
            Point rookOldPos = new Point(0,rec.getOldPos().y);
            Point rookNewPos = new Point(3,rec.getOldPos().y);

            change-=getZobristValueFor(rookOldPos, 'U', isWhite);
            change+=getZobristValueFor(rookNewPos, 'R', isWhite);
            change-=getZobristValueFor(rookNewPos);
            change+=getZobristValueFor(rookOldPos);

            change-=getZobristValueFor(rec.getOldPos(),'T', isWhite);
            change+=getZobristValueFor(rec.getNewPos(),'K', isWhite);
            change-=getZobristValueFor(rec.getNewPos());
            change+=getZobristValueFor(rec.getOldPos());
        }else if(rec.isSmallCastle())
        {
            Point rookOldPos = new Point(7,rec.getOldPos().y);
            Point rookNewPos = new Point(5,rec.getOldPos().y);

            change-=getZobristValueFor(rookOldPos, 'U', isWhite);
            change+=getZobristValueFor(rookNewPos, 'R', isWhite);
            change-=getZobristValueFor(rookNewPos);
            change+=getZobristValueFor(rookOldPos);

            change-=getZobristValueFor(rec.getOldPos(),'T', isWhite);
            change+=getZobristValueFor(rec.getNewPos(),'K', isWhite);
            change-=getZobristValueFor(rec.getNewPos());
            change+=getZobristValueFor(rec.getOldPos());
        }else if(rec.isPriseEnPassant())
        {
            Point capturePos = new Point(rec.getOldPos().x, rec.getNewPos().y);

            change-=getZobristValueFor(capturePos, 'S', !isWhite);
            change+=getZobristValueFor(capturePos);

            change-=getZobristValueFor(rec.getOldPos(), 'P', isWhite);
            change+=getZobristValueFor(rec.getOldPos());
            change-=getZobristValueFor(rec.getNewPos());
            change+=getZobristValueFor(rec.getNewPos(), 'P', isWhite);
        }else
        {
            Point op = rec.getOldPos();
            Point np = rec.getNewPos();
            char capture = rec.getCapture();
            char promotion = rec.getPromotion();

            if(rec.hasNeverMoved())
            {
                if(pieceType == 'R')
                    change-=getZobristValueFor(op, 'U', isWhite);
                else if(pieceType == 'K')
                    change-=getZobristValueFor(op, 'T', isWhite);
                else
                    change-=getZobristValueFor(op, pieceType, isWhite);
            }else
            {
                change-=getZobristValueFor(op, op.equals(rec.getEnPassantCandidate()) ? 'S' : pieceType, isWhite);
            }
            change+=getZobristValueFor(op);
            if(capture == ' ')
            {
                change-=getZobristValueFor(np);
            }else
            {
                change-=getZobristValueFor(np, capture, !isWhite);
            }
            change+=getZobristValueFor(np, promotion == ' ' ? pieceType : promotion, isWhite);
        }
        if(newEnPassantCandidate != null)
        {
            change-=getZobristValueFor(newEnPassantCandidate, 'P', isWhite);
            change+=getZobristValueFor(newEnPassantCandidate, 'S', isWhite);
        }
        return change;
    }

    public EvaluationRecord get(long hash)
    {
        int mask = chosenSize == TableSize.P30 ? 0x3FFFFFFF : chosenSize == TableSize.P25 ? 0x1FFFFFF : 0xFFFFF;
        int index = (int)(hash&mask);
        ArrayList<EvaluationRecord> list = zobristMap.get(index);
        if(list ==null)
            return null;
        for(EvaluationRecord ev : list)
        {
            if (ev.getZobristHash() == hash)
                return ev;
        }
        return null;
    }

    public void put(long hash, EvaluationRecord ev)
    {
        int mask = chosenSize == TableSize.P30 ? 0x3FFFFFFF : chosenSize == TableSize.P25 ? 0x1FFFFFF : 0xFFFFF;
        int index = (int)(hash&mask);
        ArrayList<EvaluationRecord> list = zobristMap.get(index);
        if(list == null)
        {
            zobristMap.set(index, new ArrayList<>());
            zobristMap.get(index).add(ev);
        }else if(list.contains(ev))
        {
            int subIndex = list.indexOf(ev);
            if(list.get(subIndex).hashCode() != ev.hashCode())
            {
                //TODO mettre à jour les valeurs si meilleures

            }
        }else
        {
            zobristMap.get(index).add(ev);
        }
    }
}
