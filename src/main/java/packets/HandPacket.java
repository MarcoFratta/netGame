package packets;

import cards.Card;

import java.util.List;

public class HandPacket implements Packet{

    private static final long serialVersionUID = -6245066612501284038L;
    private final List<Card> cardImpls;

    public HandPacket(List<Card> cardImpls) {
        this.cardImpls = cardImpls;
    }


    public List<Card> getCards() {
        return this.cardImpls;
    }
}
