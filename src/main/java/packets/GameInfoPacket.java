package packets;

import core.LocalPlayer;

import java.util.List;

public class GameInfoPacket implements Packet {

    private static final long serialVersionUID = -8508392439592816402L;
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
        return this.deckType;
    }

    public List<LocalPlayer> getPlayers() {
        return this.players;
    }

    public int getDeckSize() {
        return this.deckSize;
    }

    public int getHandSize() {
        return this.handSize;
    }

    public int getFieldSize() {
        return this.fieldSize;
    }

    public int getDestPlayerId() {
        return this.destPlayerId;
    }
}
