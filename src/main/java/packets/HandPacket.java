package packets;

import cards.Card;
import cards.CardImpl;

import java.util.List;

public class HandPacket implements Packet{

    private List<Card> cardImpls;

    public HandPacket(List<Card> cardImpls) {
        this.cardImpls = cardImpls;
    }

    public List<Card> getCards() {
        return cardImpls;
    }
}
