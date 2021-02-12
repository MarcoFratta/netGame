package rules;

import core.NetPlayer;
import core.Player;

import java.util.List;
import java.util.function.Function;

public interface GameRules {
    int getHandSize();
    int getMaxPLayers();
    int getMinPLayers();
    int getDeckSize();
    String getDeckPath();
    Function<List<NetPlayer>,Player> getFirstPlayer();
    int getFieldSize();
}
