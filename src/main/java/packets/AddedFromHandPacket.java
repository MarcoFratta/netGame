package packets;

import cards.Card;
import core.Pair;

public class AddedFromHandPacket implements Packet {


    private static final long serialVersionUID = -4287700987698357032L;
    private final Card card;
    private final Pair<Integer, Integer> dest;

    public AddedFromHandPacket(Card card, Pair<Integer, Integer> destPos) {
        this.card = card;
        this.dest = destPos;
    }

    public Card getCard() {
        return this.card;
    }

    public Pair<Integer, Integer> getDest() {
        return this.dest;
    }
}
