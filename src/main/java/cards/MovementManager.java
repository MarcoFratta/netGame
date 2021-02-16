package cards;


import core.Pair;
import core.State;
import net.Utilities;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MovementManager {

    private final List<List<List<Direction.Orientation>>> movements;
    private final boolean combinate;
    private final Condition condition;
    private List<Pair<Integer, Integer>> offsets;

    public MovementManager(int h, int v, int d, boolean c, boolean b) {
        this.combinate = c;
        this.movements = new ArrayList<>();
        this.combineList(h, v, d);
        this.condition = b ? Condition.START_ON_BOARD : Condition.EVERYWHERE;  // change to args from file
    }

    private void combineList(int h, int v, int d) {
        this.movements.add(Direction.VERTICAL.getDirections().stream()
                .map(dir -> Stream.iterate(dir, a -> a)
                        .limit(v).collect(Collectors.toList())).collect(Collectors.toList()));
        this.movements.add(Direction.HORIZONTAL.getDirections().stream()
                .map(dir -> Stream.iterate(dir, a -> a)
                        .limit(h).collect(Collectors.toList())).collect(Collectors.toList()));
        this.movements.add(Direction.DIAGONAL.getDirections().stream()
                .map(dir -> Stream.iterate(dir, a -> a)
                        .limit(d).collect(Collectors.toList())).collect(Collectors.toList()));

        this.offsets = this.combinate ? this.withCombination() : this.withoutCombination();

    }

    public boolean canMoveTo(Pair<Integer, Integer> start, Pair<Integer, Integer> dest, int size) {

        //System.out.println("Want to move from -> "+start+",to -> "+dest);
        return this.getDestinations(State.FIELD, start, size).contains(dest);
    }

    public List<Pair<Integer, Integer>> getDestinations(State state, Pair<Integer, Integer> start, int size) {
        switch (state) {
            case HAND -> {
                List<Pair<Integer, Integer>> res = new ArrayList<>();
                IntStream.range(0, size).forEach(i -> IntStream.range(0, size)
                        .forEach(j -> res.add(new Pair<>(i, j))));
                return res.stream().filter(a -> this.condition.getCondition().test(a, size)).collect(Collectors.toList());
            }
            case FIELD -> {
                this.movements.forEach(System.out::println);
                return this.fromMovements(start).stream()
                        .filter(p -> (p.getX() >= 0 && p.getX() < size) &&
                                (p.getY() >= 0 && p.getY() < size))
                        .peek(System.out::println).collect(Collectors.toList());
            }
        }
        return null;
    }

    private List<Pair<Integer, Integer>> fromMovements(Pair<Integer, Integer> start) {
        return this.offsets.stream()
                .map(a -> new Pair<>(a.getX() + start.getX(), a.getY() + start.getY()))
                .collect(Collectors.toList());
    }

    private List<Pair<Integer, Integer>> withoutCombination() {
        return this.getPairFromDirections(this.movements.stream()
                .flatMap(List::stream)
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toList()));
    }

    private List<Pair<Integer, Integer>> withCombination() {

        List<List<Direction.Orientation>> combinations = new ArrayList<>();
        Utilities.permute(this.movements, perm ->
                combinations.add(perm.stream().flatMap(List::stream).collect(Collectors.toList())));

        try {
            return this.getPairFromDirections(combinations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Pair<Integer, Integer>> getPairFromDirections(List<List<Direction.Orientation>> combinations) {
        return combinations.stream()
                .map(w -> w.stream()
                        .map(d -> new Pair<>(d.getX(), d.getY()))
                        .reduce((a, b) -> new Pair<>(a.getX() + b.getX(), a.getY() + b.getY())).get())
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean canStartOn(Pair<Integer, Integer> pos, int size) {
        // System.out.println("Dest ->"+pos.toString()+"border->"+ this.condition);
        return this.condition.getCondition().test(pos, size);
    }

}
