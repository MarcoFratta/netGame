package rules;

import core.Player;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class SimpleGameRules implements GameRules{

    public static final int DEF_MIN_PLAYERS = 2;
    public static final int DEF_MAX_PLAYERS = 4;
    public static final int DEF_HAND_SIZE = 3;
    public static final int DEF_FIELD_SIZE = 3;
    public static final int DEF_DECK_SIZE = 40;

    private final int maxPlayers,minPlayers,handSize,deckSize,fieldSize;
    private final String deckPath;
    private final Function<List<Player>,Player> firstPlayer;

    public SimpleGameRules(int maxPlayers, int minPlayers, int handSize, String deckPath,
                           int deckSize, int fieldSize, Function<List<Player>,Player> function) {
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.handSize = handSize;
        this.deckPath = deckPath;
        this.deckSize = deckSize;
        this.fieldSize = fieldSize;
        this.firstPlayer = function;
    }
    public SimpleGameRules(String deckPath){
        this(DEF_MAX_PLAYERS,DEF_MIN_PLAYERS,DEF_HAND_SIZE,deckPath,DEF_DECK_SIZE,DEF_FIELD_SIZE,l -> l.get(0));
    }

    @Override
    public int getHandSize() {
        return this.handSize;
    }

    @Override
    public int getMaxPLayers() {
        return this.maxPlayers;
    }

    @Override
    public int getMinPLayers() {
        return this.minPlayers;
    }

    @Override
    public int getDeckSize() {
        return this.deckSize;
    }

    @Override
    public String getDeckPath() {
        return this.deckPath;
    }

    @Override
    public Function<List<Player>, Player> getFirstPlayer() {
        return null;
    }

    @Override
    public int getFieldSize() {
        return this.fieldSize;
    }
}
