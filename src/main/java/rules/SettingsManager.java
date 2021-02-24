package rules;

import java.util.Random;

public class SettingsManager {

    public static final int DEF_MIN_PLAYERS = 2;
    public static final int DEF_MAX_PLAYERS = 4;
    public static final int DEF_HAND_SIZE = 5;
    public static final int DEF_FIELD_SIZE = 4;
    public static final int DEF_DECK_SIZE = 40;

    private int playersNum;
    private int handCardsNum;
    private int fieldSize;
    private int deckSize;
    private String path;

    public SettingsManager() {
        this.playersNum = DEF_MIN_PLAYERS;
        this.handCardsNum = DEF_HAND_SIZE;
        this.fieldSize = DEF_FIELD_SIZE;
        this.deckSize = DEF_DECK_SIZE;

    }

    public GameRules getSettings() {
        return new SimpleGameRules(this.playersNum,
                this.playersNum,
                this.handCardsNum,
                this.path,
                this.deckSize,
                this.fieldSize,
                (a) -> a.get(new Random().nextInt(a.size())));
    }

    public int getPlayersNum() {
        return this.playersNum;
    }

    public void setPlayersNum(int playersNum) {
        this.playersNum = playersNum;
    }

    public int getHandCardsNum() {
        return this.handCardsNum;
    }

    public void setHandCardsNum(int handCardsNum) {
        this.handCardsNum = handCardsNum;
    }

    public int getFieldSize() {
        return this.fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public int getDeckSize() {
        return this.deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
