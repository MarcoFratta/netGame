package packets;

import core.LocalPlayer;
import core.Player;

import java.util.List;
import java.util.Locale;

public class GameInfoPacket implements Packet {

    private final List<LocalPlayer> players;
    private final String deckType;
    private final int deckSize;
    private final int handSize;
    private final int fieldSize;
    private final int destPlayerId;


    public GameInfoPacket(List<LocalPlayer> otherPlayers, String deckType, int deckSize, int handSize, int fieldSize,int id) {
        this.players = otherPlayers;
        this.deckType = deckType;
        this.deckSize = deckSize;
        this.handSize = handSize;
        this.fieldSize = fieldSize;
        this.destPlayerId = id;
    }

    public String getDeckType(){
        return deckType;
    }

    public List<LocalPlayer> getPlayers() {
        return players;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public int getHandSize() {
        return handSize;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getDestPlayerId() {
        return destPlayerId;
    }
}
