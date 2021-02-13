package cards;

import core.Pair;
import java.util.function.BiPredicate;

public enum Condition {

    START_ON_BOARD((pos,size)->(pos.getX()==(0) || pos.getX() == (size-1)) || // is on border
            (pos.getY() == (0) || pos.getY()== (size-1))),
    EVERYWHERE((pos,size) -> true);

    private final BiPredicate<Pair<Integer,Integer>,Integer> condition;

    Condition(BiPredicate<Pair<Integer,Integer>,Integer> condition){
        this.condition = condition;
    }

    public BiPredicate<Pair<Integer, Integer>, Integer> getCondition() {
        return this.condition;
    }
}
