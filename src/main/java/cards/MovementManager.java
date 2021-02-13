package cards;

import core.Pair;
import core.State;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MovementManager implements Serializable {

    private static final long serialVersionUID = -7437858408600977743L;
    private final Map<Direction, Integer> movements;
    private final boolean combinate;
    private final Condition condition;


    public MovementManager(int h, int v, int d, boolean c, boolean b) {
        this.combinate = c;
        this.movements = new EnumMap<>(Direction.class);
        this.movements.put(Direction.HORIZONTAL,h);
        this.movements.put(Direction.VERTICAL,v);
        this.movements.put(Direction.DIAGONAL,d);
        this.condition = b ? Condition.START_ON_BOARD : Condition.EVERYWHERE;  // change to args from file
    }

    public boolean canMoveTo(Pair<Integer,Integer> start,Pair<Integer,Integer> dest){
        return true;
    }
    public boolean canMoveTo(Pair<Integer,Integer> start ,Pair<Integer,Integer> dest,int size){
        return this.getDestinations(State.HAND,start,size).contains(dest);
    }

    public List<Pair<Integer,Integer>> getDestinations(State state, Pair<Integer, Integer> start,int size){
        switch (state){
            case HAND: {
                List<Pair<Integer,Integer>> res = new ArrayList<>();
                IntStream.range(0,size).forEach(i->IntStream.range(0,size)
                .forEach(j-> res.add(new Pair<>(i,j))));
                return res.stream().filter(a-> this.condition.getCondition().test(a,size)).collect(Collectors.toList());
            }
            case FIELD:{
                return this.fromMovements().stream()
                        .filter(p -> (p.getX() >= 0 && p.getX() <size) &&
                                (p.getY()>=0 && p.getY()<size)).collect(Collectors.toList());
            }
        }
        return null;
    }

    private List<Pair<Integer, Integer>> fromMovements() {
        return Collections.emptyList();
    }

    public boolean canStartOn(Pair<Integer,Integer>pos, int size){
        System.out.println("Dest ->"+pos.toString()+"border->"+ this.condition);
        return this.condition.getCondition().test(pos,size);
    }

}
