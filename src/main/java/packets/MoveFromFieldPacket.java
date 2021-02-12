package packets;

import core.Pair;

public class MoveFromFieldPacket implements Packet {
    private final Pair<Integer, Integer> start;
    private final Pair<Integer, Integer> dest;

    public MoveFromFieldPacket(Pair<Integer, Integer> start, Pair<Integer, Integer> dest) {
        this.start = start;
        this.dest= dest;
    }

    public Pair<Integer, Integer> getStart() {
        return start;
    }

    public Pair<Integer, Integer> getDest() {
        return dest;
    }
}
