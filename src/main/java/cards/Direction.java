package cards;

import core.Pair;

import java.util.Arrays;
import java.util.Set;

public enum Direction {
    VERTICAL(Set.of(Orientation.UP, Orientation.DOWN)),
    HORIZONTAL(Set.of(Orientation.RIGHT, Orientation.LEFT)),
    DIAGONAL(Set.of(Orientation.UP_DIAGONAL_RIGHT, Orientation.UP_DIAGONAL_LEFT,
            Orientation.DOWN_DIAGONAL_RIGHT, Orientation.DOWN_DIAGONAL_LEFT));

    private final Set<Orientation> directions;

    Direction(Set<Orientation> directions) {
        this.directions = directions;
    }

    public Set<Orientation> getDirections() {
        return Set.copyOf(this.directions);
    }

    enum Orientation {
        UP(0, -1),
        DOWN(0, 1),
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP_DIAGONAL_RIGHT(1, -1),
        UP_DIAGONAL_LEFT(-1, -1),
        DOWN_DIAGONAL_RIGHT(1, 1),
        DOWN_DIAGONAL_LEFT(-1, 1);

        private final int x;
        private final int y;

        Orientation(int i, int i1) {
            this.x = i;
            this.y = i1;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public Direction getType() {
            return Arrays.stream(Direction.values())
                    .filter(a -> a.getDirections().contains(this)).findFirst().get();
        }
    }


}
