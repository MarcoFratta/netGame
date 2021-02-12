package packets;

import core.Pair;

public class MovePacket implements Packet {
    private Pair<Integer,Integer> start,dest;
    public MovePacket(Pair<Integer, Integer> start, Pair<Integer, Integer> destination) {
        this.start = start;
        this.dest = destination;
    }

    public Pair<Integer, Integer> getStart() {
        return start;
    }

    public Pair<Integer, Integer> getDest() {
        return dest;
    }
}
