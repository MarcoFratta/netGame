package packets;

import cards.Card;
import core.Pair;

public class MoveFromHandPacket implements Packet {
    private static final long serialVersionUID = 147213949735545915L;
    private final Card selectedHandCard;
    private final Pair<Integer, Integer> destPos;

    public MoveFromHandPacket(Card selectedHandCard, Pair<Integer, Integer> pos) {

        this.selectedHandCard = selectedHandCard;
        this.destPos = new Pair<>(pos);
        //System.out.println("Selected hand card ->" +selectedHandCard+" dest -> "+destPos);
    }

    public Card getSelectedHandCard() {
        return this.selectedHandCard;
    }

    public Pair<Integer, Integer> getDestPos() {
        return this.destPos;
    }
}
