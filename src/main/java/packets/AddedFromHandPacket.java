package packets;

import cards.Card;
import core.Pair;

import java.util.Optional;

public class AddedFromHandPacket implements Packet {

    private final Card card;
    private final Pair<Integer, Integer> dest;

    public AddedFromHandPacket(Card card, Pair<Integer, Integer> destPos) {
        this.card = card;
        this.dest = destPos;
    }

    public Card getCard() {
        return card;
    }

    public Pair<Integer, Integer> getDest() {
        return dest;
    }
}
