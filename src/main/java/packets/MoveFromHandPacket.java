package packets;

import core.Pair;

public class MoveFromHandPacket implements Packet {
    private final int selectedHandCard;
    private final Pair<Integer, Integer> destPos;

    public MoveFromHandPacket(int selectedHandCard, Pair<Integer, Integer> pos) {

        this.selectedHandCard = selectedHandCard;
        this.destPos = pos;
    }

    public int getSelectedHandCard() {
        return selectedHandCard;
    }

    public Pair<Integer, Integer> getDestPos() {
        return destPos;
    }
}
