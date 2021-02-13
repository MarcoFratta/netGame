package packets;

import core.Pair;

public class MoveFromFieldPacket implements Packet {
    private static final long serialVersionUID = -9195805864768504276L;
    private final Pair<Integer, Integer> start;
    private final Pair<Integer, Integer> dest;

    public MoveFromFieldPacket(Pair<Integer, Integer> start, Pair<Integer, Integer> dest) {
        this.start = new Pair<>(start);
        this.dest= new Pair<>(dest);
    }

    public Pair<Integer, Integer> getStart() {
        return this.start;
    }

    public Pair<Integer, Integer> getDest() {
        return this.dest;
    }

    /*@Override
    public String toString() {
        return "Move from "+start.toString()+" to "+dest.toString();
    }

     */
}
