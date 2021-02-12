package cards;

import core.Pair;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MovementManager implements Serializable {

    private final Map<Direction, Integer> movements;
    private final boolean combinate;
    protected final boolean startOnBorder;


    public MovementManager(int h, int v, int d, boolean c, boolean b) {
        this.combinate = c;
        this.startOnBorder = b;
        movements = new EnumMap<>(Direction.class);
        movements.put(Direction.HORIZONTAL,h);
        movements.put(Direction.VERTICAL,v);
        movements.put(Direction.DIAGONAL,d);
    }

    public boolean canMoveTo(Pair<Integer,Integer> start,Pair<Integer,Integer> dest){
        return true;
    }

    public List<Pair<Integer,Integer>> getDestinations(Pair<Integer,Integer> start){
        return null;
    }

    public boolean canStartOn(Pair<Integer,Integer>pos, int size){
        System.out.println("Dest ->"+pos.toString()+"border->"+startOnBorder);
        if(this.startOnBorder){
            return (pos.getX()==(0) || pos.getX() == (size-1)) || // is on border
                    (pos.getY() == (0) || pos.getY()== (size-1));
        } else return true;
    }

}
